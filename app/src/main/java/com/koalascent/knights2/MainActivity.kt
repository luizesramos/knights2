package com.koalascent.knights2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.koalascent.knights2.util.Logger
import com.koalascent.knights2.util.ResourceProviderWrapper
import com.koalascent.knights2.viewmodel.SceneCoordinator

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */

class MainActivity : AppCompatActivity() {

    private lateinit var coordinator: SceneCoordinator
    private lateinit var sceneView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sceneView = findViewById(R.id.mapImageView)
        sceneView.setOnTouchListener(touchListener)

        coordinator = ViewModelProviders.of(this).get(SceneCoordinator::class.java)

        coordinator.redrawObservable.observe(this, Observer { bitmap ->
            sceneView.setImageBitmap(bitmap)
        })

        coordinator.tickObservable.observe(this, Observer { delay ->
            sched.postDelayed(runnable, delay)
        })

        // creating a scene bitmap with the dimensions of the screen
        try {
            coordinator.setup(ResourceProviderWrapper(this.applicationContext))
        } catch (e: Exception) {
            Logger.e("Decoding Knight Bitmap", e)
            finish()
        }
    }

    private val sched = Handler(Looper.getMainLooper())

    private val runnable = Runnable {
        coordinator.step()
    }

    private val touchListener = View.OnTouchListener { _: View, event: MotionEvent ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            coordinator.touch(event.x.toInt(), event.y.toInt())
        }
        false
    }
}
