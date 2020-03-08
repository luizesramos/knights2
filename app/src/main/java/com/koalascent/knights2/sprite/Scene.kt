package com.koalascent.knights2.sprite

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import java.io.InputStream

class Scene {

    // dimensions of the view
    var width: Int
        private set
    var height: Int
        private set

    private var sceneBitmap: Bitmap? = null

    private lateinit var mCanvas: Canvas
    private lateinit var clearPaint: Paint

    private var drawPaint: Paint? = null

    constructor(context: Context) {
        val metrics = context.resources.displayMetrics
        width = metrics.widthPixels
        height = metrics.heightPixels
        initScreen()
    }

    constructor(stream: InputStream?) {
        // read the entire raw sprite file
        val opts = BitmapFactory.Options()
        opts.inScaled = true
        opts.inJustDecodeBounds = true
        BitmapFactory.decodeStream(stream, null, opts)
        height = opts.outHeight
        width = opts.outWidth
        initScreen()
    }

    private fun initScreen() {
        sceneBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas().apply { setBitmap(sceneBitmap) }
        clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        drawPaint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG or Paint.ANTI_ALIAS_FLAG)
        resetScene()
    }

    fun resetScene() {
        mCanvas.apply {
            drawPaint(clearPaint)
            drawColor(0x200000a0) // DEBUG
        }
    }

    fun placeAvatar(x: Int, y: Int, bmp: Bitmap?) {
        mCanvas.drawBitmap(bmp!!, x.toFloat(), y.toFloat(), drawPaint)
    }

    fun draw(scene: ImageView) {
        scene.setImageBitmap(sceneBitmap)
    }
}