package com.koalascent.knights2.sprite

import android.graphics.*
import java.io.InputStream

class RawSprite(stream: InputStream?, unitsWidthwise: Int, unitsHeightwise: Int) {
    private val rawBitmap: Bitmap?
    private val mBitmap: Bitmap

    // raw width and height of entire sprite file
    private val w: Int
    private val h: Int

    // dimensions of a unit inside the file
    val width: Int
    val height: Int
    private var frame = 0
    private val maxFrames: Int
    private val canvas: Canvas
    private val paint: Paint

    fun getFrame(): Int {
        return frame
    }

    fun setFrame(i: Int) {
        if (i < 0 || i >= maxFrames) return
        frame = i
    }

    fun getCurrentBitmap(reverse: Boolean): Bitmap {
        val row = frame * width / w * height
        val col = frame * width % w
        // parent.quickMessage("ROW:" + row + " COL:" + col);
        val view = Rect(0, 0, width, height)
        val clip =
            Rect(col, row, col + width, row + height)
        canvas.setBitmap(mBitmap)
        canvas.save()
        if (reverse) {
            canvas.translate(mBitmap.width.toFloat(), 0f)
            canvas.scale(
                -scalingFactor.toFloat(),
                scalingFactor.toFloat()
            )
        } else {
            canvas.scale(
                scalingFactor.toFloat(),
                scalingFactor.toFloat()
            )
        }

        rawBitmap?.let {
            canvas.drawBitmap(it, clip, view, paint)
        }
        canvas.restore()
        return mBitmap
    }

    companion object {
        private const val scalingFactor = 2
    }

    init {
        // read the entire raw sprite file
        val opts = BitmapFactory.Options()
        opts.inScaled = true
        rawBitmap = BitmapFactory.decodeStream(stream, null, opts)

        // get dimensions
        h = opts.outHeight
        w = opts.outWidth
        height = h / unitsHeightwise
        width = w / unitsWidthwise
        maxFrames = unitsHeightwise * unitsWidthwise
        // parent.showMessage("ROWS:" + h / unitHeight + " W:" + w / unitWidth);

        // paint
        paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)

        // parent.showMessage("UH:" + unitHeight + " UW:" + unitWidth);
        mBitmap = Bitmap.createBitmap(
            width * scalingFactor, height
                    * scalingFactor, Bitmap.Config.ARGB_8888
        )
        canvas = Canvas()
        setFrame(0)
    }
}