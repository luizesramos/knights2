package com.koalascent.knights2.graph

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

internal class AStar constructor(
    private val nodes: HashSet<Pos?>,
    private val neighborProvider: NeighborProvider = DefaultNeighborProvider
) {
    // Set of nodes under evaluation
    private val openSet = HashSet<Pos?>()

    // For node n, cameFrom[n] is the node immediately preceding it on the cheapest path from start to n currently known.
    private val cameFrom = HashMap<Pos?, Pos?>()

    // For node n, gScore[n] is the cost of the cheapest path from start to n currently known.
    private val gScore = HashMap<Pos?, Double>()

    // For node n, fScore[n] := gScore[n] + h(n).
    private val fScore = HashMap<Pos?, Double>()

    fun shortestPath(start: Pos, goal: Pos): ArrayList<Pos?> {
        openSet.clear()
        openSet.add(start)
        gScore[start] = 0.0
        fScore[start] = heuristicDistance(start, goal)
        while (openSet.isNotEmpty()) {
            val current = lowestFScoreInOpenSet()
            if (current == null || current === goal) {
                return reconstructPath(goal)
            }
            openSet.remove(current)
            for (direction in neighborProvider.allDirections) {
                val neighbor = neighborProvider.neighborAtDirection(current, direction)
                if (neighbor == null || !nodes.contains(neighbor)) {
                    continue
                }
                // d(current,neighbor) is the weight of the edge from current to neighbor
                // tentative_gScore is the distance from start to the neighbor through current
                val tentativeGScore = (gScore[current] ?: 0.0) + edgeDistance //edgeDistance(current, neighbor)
                val gScoreNeighbor = (if (gScore.containsKey(neighbor)) gScore[neighbor] else Double.MAX_VALUE)!!
                if (tentativeGScore < gScoreNeighbor) {
                    // This path to neighbor is better than any previous one. Record it!
                    cameFrom[neighbor] = current
                    gScore[neighbor] = tentativeGScore
                    fScore[neighbor] = tentativeGScore + heuristicDistance(neighbor, goal)
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor)
                    }
                }
            }
        }
        return reconstructPath(goal)
    }

    // euclidean distance as approximate distance
    private fun heuristicDistance(a: Pos, b: Pos): Double {
        return sqrt((a.x - b.x.toDouble()).pow(2.0) + (a.y - b.y.toDouble()).pow(2.0))
    }

    // edge distance between two nodes (make generic one day)
    private val edgeDistance = 1.0
//    private fun edgeDistance(a: Pos?, b: Pos?): Double {
//        return 1.0
//    }

    private fun lowestFScoreInOpenSet(): Pos? {
        var minScore = Double.MAX_VALUE
        var minScorePos: Pos? = null
        for (pos in openSet) {
            if (fScore.containsKey(pos)) {
                fScore[pos]?.let {
                    if (it < minScore) {
                        minScore = it
                        minScorePos = pos
                    }
                }
            }
        }
        return minScorePos
    }

    private fun reconstructPath(goal: Pos): ArrayList<Pos?> {
        val path =
            ArrayList<Pos?>()
        path.add(goal)
        var current: Pos? = goal
        while (cameFrom.containsKey(current)) {
            current = cameFrom[current]
            path.add(0, current)
        }
        return path
    }
}