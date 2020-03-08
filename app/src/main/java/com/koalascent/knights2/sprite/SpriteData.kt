package com.koalascent.knights2.sprite

class SpriteData(private val screenWidth: Int, private val screenHeight: Int) {
    var x: Int
        private set
    var y: Int
        private set

    private val velX: Int
    private val velY: Int
    private var targetX = 0
    private var targetY = 0
    private val mt: MoveTranslator

    private fun parseDirection() {
        mt.clearStatus()

        // find horizontal direction
        if (targetX > x) {
            mt.set(MoveTranslator.GO_RG)
        } else if (targetX < x) {
            mt.set(MoveTranslator.GO_LF)
        }

        // find vertical direction
        if (targetY > y) {
            mt.set(MoveTranslator.GO_DN)
        } else if (targetY < y) {
            mt.set(MoveTranslator.GO_UP)
        }

        //android.util.Log.v("SpriteData", "POS(" + posX + "," + posY + ") TGT("
        //		+ targetX + "," + targetY + ")");
    }

    fun setTarget(x: Int, y: Int) {
        // ignore target if either x or y are out-of-bounds
        if (x < 0 || x > screenWidth || y < 0 || y > screenHeight) return
        targetX = x
        targetY = y
        parseDirection()
    }

    // positions the avatar in the correct direction
    // returns the index of the
    fun walk(): Int {
        // stop condition: rounding up the arrival
        if (Math.abs(targetX - x) < velX) {
            targetX = x
        }
        if (Math.abs(targetY - y) < velY) {
            targetY = y
        }

        // verify if there is movement
        parseDirection()

        // mtlate
        if (mt.direction(MoveTranslator.GO_UP)) {
            y -= velY
        } else if (mt.direction(MoveTranslator.GO_DN)) {
            y += velY
        }
        if (mt.direction(MoveTranslator.GO_RG)) {
            x += velX
        } else if (mt.direction(MoveTranslator.GO_LF)) {
            x -= velX
        }

        // return the new frame index
        return mt.nextFrameIndex
    }

    val reverse: Boolean
        get() = mt.direction(MoveTranslator.GO_LF)

    fun stopped(): Boolean {
        return mt.isClear
    }

    companion object {
        private const val SCREEN_BORDERS = 10
    }

    init {
        x = screenWidth / 2
        y = screenHeight / 2
        velX = 16
        velY = 16
        mt = MoveTranslator()
        setTarget(x, y) // start stopped
    }
}