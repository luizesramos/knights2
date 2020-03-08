package com.koalascent.knights2.graph

import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

class Maze(val nodes: HashMap<Pos, Pos> = HashMap())

interface MazeBuilder {
    fun build(): Maze
}

class HexMazeBuilder(private val canvasWidth: Int,
                     private val canvasHeight: Int,
                     private val hexRadius: Int): MazeBuilder {

    override fun build(): Maze {
        val nodes: HashMap<Pos, Pos> = HashMap()

        val cols = ceil(sqrt(hexRadius.toDouble())).toInt()
        val rows = ceil(hexRadius / cols.toDouble()).toInt()
        val padX = ceil(canvasWidth.toDouble() / cols).toInt()
        val padY = ceil(canvasHeight.toDouble() / rows).toInt()
        val spaceX = ceil((canvasWidth.toDouble() - 2 * padX) / cols).toInt()
        val spaceY = ceil((canvasHeight.toDouble() - 2 * padY) / rows).toInt()

        for (y in 0 until rows) {
            for (x in 0 until cols) {
                val pos = Pos(x, y)
                val element = Pos(padX + x * spaceX, padY + y * spaceY)
                nodes[pos] = element
            }
        }

        return Maze(nodes)
    }
}