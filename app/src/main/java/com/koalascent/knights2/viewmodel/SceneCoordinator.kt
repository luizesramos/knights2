package com.koalascent.knights2.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.koalascent.knights2.graph.HexMazeBuilder
import com.koalascent.knights2.graph.Maze
import com.koalascent.knights2.graph.MazeBuilder
import com.koalascent.knights2.sprite.RawSprite
import com.koalascent.knights2.sprite.Scene
import com.koalascent.knights2.sprite.SpriteData
import com.koalascent.knights2.util.ResourceProvider
import java.io.InputStream

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

class SceneCoordinator: ViewModel() {

    //private lateinit var maze: Maze
    private lateinit var avatar: SpriteData
    private lateinit var avatarSprite: RawSprite
    private lateinit var scene: Scene
    private var stepsThreshold = 0

    val redrawObservable = MutableLiveData<Bitmap?>()
    val tickObservable = MutableLiveData<Long>()

    @Throws
    fun setup(resourceProvider: ResourceProvider) {
        scene = Scene(resourceProvider.screenWidth(), resourceProvider.screenHeight())

        //maze = HexMazeBuilder(resourceProvider.screenWidth(), resourceProvider.screenHeight(), 10).build()

        // creating the avatar's position and current frame data
        avatar = SpriteData(scene.width, scene.height)

        // creating avatar images from asset sprite file
        val stream: InputStream = resourceProvider.openAsset("knightwalk.png")
        avatarSprite = RawSprite(stream, 5, 5)
        stream.close()

        // creating handler for automatic movements + safety threshold
        resetStepThreshold()

        // draw everything
        redraw()
    }

    private fun redraw() {
        scene.resetScene()
        avatarSprite.setFrame(avatar.walk())
        scene.placeAvatar(avatar.x, avatar.y, avatarSprite.getCurrentBitmap(avatar.reverse))
        redrawObservable.postValue(scene.sceneBitmap)
    }

    // the avatar moves this many steps at a time
    private fun resetStepThreshold() {
        stepsThreshold = DEFAULT_MAX_STEPS
    }

    fun step() {
        stepsThreshold--
        if (!avatar.stopped() && stepsThreshold > 0) {
            redraw()
            tickObservable.postValue(WALK_DELAY)
        }
    }

    fun touch(touchX: Int, touchY: Int) {
        resetStepThreshold()
        val x = touchX - avatarSprite.width / 2
        val y = touchY + avatarSprite.height / 2
        avatar.setTarget(x, y)
        tickObservable.postValue(WALK_DELAY)
    }

    companion object {
        private const val WALK_DELAY = 500L
        private const val DEFAULT_MAX_STEPS = 100
    }
}