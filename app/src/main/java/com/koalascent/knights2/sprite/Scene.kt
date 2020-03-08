package com.koalascent.knights2.sprite

import android.graphics.*
import java.io.InputStream

/**
 * (c) 2020 Luiz Ramos All Rights Reserved
 */
class Scene {

    // dimensions of the view
    var width: Int
        private set
    var height: Int
        private set

    var sceneBitmap: Bitmap? = null
        private set

    private lateinit var mCanvas: Canvas
    private lateinit var clearPaint: Paint

    private var drawPaint: Paint? = null

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
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
        }
    }

    fun placeAvatar(x: Int, y: Int, bmp: Bitmap?) {
        bmp?.let {
            mCanvas.drawBitmap(it, x.toFloat(), y.toFloat(), drawPaint)
        }
    }
}