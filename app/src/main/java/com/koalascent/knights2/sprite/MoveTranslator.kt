package com.koalascent.knights2.sprite

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */
class MoveTranslator {
    private var dirIndex: Int
    private var spriteIndex: Int
    private val moves: Array<IntArray>
    private var status = 0

    // flag methods
    fun clearStatus() {
        status = CLEAR
    }

    fun set(flag: Int) {
        status = status or flag
    }

    fun direction(flag: Int): Boolean {
        return status and flag != CLEAR
    }

    val isClear: Boolean
        get() = status == CLEAR// advance sprite index

    // translates a move intent into a frame index
    val nextFrameIndex: Int
        get() {
            if (isClear) {
                dirIndex = DIR_DN
                spriteIndex = 0
            } else {
                // advance sprite index
                spriteIndex = (spriteIndex + 1) % INDICES_PER_DIRECTION
                if (spriteIndex == 0) spriteIndex = 1

                dirIndex = if (direction(GO_UP)) {
                    if (direction(GO_RG) || direction(GO_LF)) DIR_UP_RGLF else DIR_UP
                } else if (direction(GO_DN)) {
                    if (direction(GO_RG) || direction(GO_LF)) DIR_DN_RGLF else DIR_DN
                } else {
                    DIR_RG_LF
                }
            }

            return moves[dirIndex][spriteIndex]
        }

    companion object {
        const val CLEAR = 0x00
        const val GO_UP = 0x01
        const val GO_RG = 0x02
        const val GO_DN = 0x04
        const val GO_LF = 0x08

        // direction -> index translation
        private const val DIR_UP = 0
        private const val DIR_UP_RGLF = 1
        private const val DIR_RG_LF = 2
        private const val DIR_DN_RGLF = 3
        private const val DIR_DN = 4
        private const val DIRECTIONS = 5
        private const val INDICES_PER_DIRECTION = 5
    }

    init {
        dirIndex = DIR_DN
        spriteIndex = 0

        // movement matrix
        moves = Array(DIRECTIONS) { IntArray(INDICES_PER_DIRECTION) }

        for (i in 0 until DIRECTIONS) {
            moves[i][0] = i
            for (j in 1 until INDICES_PER_DIRECTION) {
                moves[i][j] = moves[i][j - 1] + INDICES_PER_DIRECTION
            }
        }
        clearStatus()
    }
}