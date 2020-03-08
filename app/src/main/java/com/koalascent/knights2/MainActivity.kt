package com.koalascent.knights2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.koalascent.knights2.sprite.RawSprite
import com.koalascent.knights2.sprite.Scene
import com.koalascent.knights2.sprite.SpriteData
import com.koalascent.knights2.util.Logger
import java.io.InputStream

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

class MainActivity : AppCompatActivity(), Runnable, View.OnTouchListener {

    companion object {
        private const val TAG = "Knight"
        private const val WALK_DELAY = 500L
        private const val DEFAULT_MAX_STEPS = 30
    }

    private val sched = Handler(Looper.getMainLooper())
    private lateinit var sceneView: ImageView
    private lateinit var avatar: SpriteData
    private lateinit var avatarSprite: RawSprite
    private lateinit var scene: Scene
    private var stepsThreshold = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setting up the image view
        sceneView = findViewById(R.id.mapImageView)
        sceneView?.setOnTouchListener(this)

        // creating a scene bitmap with the dimensions of the screen
        scene = Scene(this)

        // creating the avatar's position and current frame data
        avatar = SpriteData(scene.width, scene.height)

        // creating avatar images from asset sprite file
        try {
            val stream: InputStream = assets.open("knightwalk.png")
            avatarSprite = RawSprite(stream, 5, 5)
            stream.close()
        } catch (e: Exception) {
            Logger.e("Decoding Knight Bitmap", e)
            finish()
        }

        // creating handler for automatic movements + safety threshold
        resetStepThreshold()

        // draw everything
        redraw()
    }

    private fun redraw() {
        scene.resetScene()
        avatarSprite.setFrame(avatar.walk())
        scene.placeAvatar(avatar.x, avatar.y,
                avatarSprite.getCurrentBitmap(avatar.reverse))
        scene.draw(sceneView)
        // android.util.Log.v(TAG, "FRAME: " + avatarSprite.getFrame());
    }

    fun showMessage(s: String?) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
    }

    fun quickMessage(s: String?) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    fun prevClick(v: View?) {
        avatar.randomMove()
    }

    // the avatar moves this many steps at a time
    private fun resetStepThreshold() {
        stepsThreshold = DEFAULT_MAX_STEPS
    }

    override fun run() {
        stepsThreshold--
        if (!avatar.stopped() && stepsThreshold > 0) {
            redraw()
            sched.postDelayed(this, WALK_DELAY)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val x = event.x.toInt() - avatarSprite.width / 2
        val y = event.y.toInt() + avatarSprite.height / 2
        if (event.action == MotionEvent.ACTION_DOWN) {
            // DEBUG: put something on the screen
            // android.util.Log.v(TAG, "TOUCH X:" + x + " Y:" + y);
            // scene.placeAvatar(x, y,
            // avatarSprite.getCurrentBitmap(avatar.getReverse()));
            // scene.draw(sceneView);
            resetStepThreshold()
            avatar.setTarget(x, y)
            sched.postDelayed(this, WALK_DELAY)
        }
        return false
    }
}
