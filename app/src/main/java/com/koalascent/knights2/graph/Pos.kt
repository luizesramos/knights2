package com.koalascent.knights2.graph

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

class Pos(var x: Int, var y: Int) {

    override fun toString(): String {
        return "($x, $y)"
    }

    override fun hashCode(): Int {
        return x * 13 + y * 71
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other === this) {
            return true
        }
        val p = other as Pos
        return p.x == x && p.y == y
    }
}