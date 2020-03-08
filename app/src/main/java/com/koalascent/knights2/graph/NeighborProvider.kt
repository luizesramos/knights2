package com.koalascent.knights2.graph

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

interface NeighborProvider {
    val allDirections: Array<MoveDirection>
    fun neighborAtDirection(current: Pos, direction: MoveDirection): Pos?
}

object DefaultNeighborProvider: NeighborProvider {

    override val allDirections = arrayOf(
        MoveDirection.North,
        MoveDirection.West,
        MoveDirection.East,
        MoveDirection.South
    )

    override fun neighborAtDirection(current: Pos, direction: MoveDirection): Pos? {
        return when (direction) {
            MoveDirection.North -> Pos(current.x, current.y - 1)
            MoveDirection.West -> Pos(current.x - 1, current.y)
            MoveDirection.South -> Pos(current.x, current.y + 1)
            MoveDirection.East -> Pos(current.x + 1, current.y)
            else -> null
        }
    }
}