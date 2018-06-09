package com.example.jesse.qrscanner

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.example.jesse.qrscanner.ui.camera.GraphicOverlay
import com.google.android.gms.vision.barcode.Barcode


class QrCodeGraphic internal constructor(overlay: GraphicOverlay<*>) : GraphicOverlay.Graphic(overlay as GraphicOverlay<GraphicOverlay.Graphic>) {

    var id: Int = 0

    private val mRectPaint: Paint
    private val mTextPaint: Paint
    @Volatile
    var barcode: Barcode? = null
        private set

    init {

        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.size
        val selectedColor = COLOR_CHOICES[mCurrentColorIndex]

        mRectPaint = Paint()
        mRectPaint.color = selectedColor
        mRectPaint.style = Paint.Style.STROKE
        mRectPaint.strokeWidth = 4.0f

        mTextPaint = Paint()
        mTextPaint.color = selectedColor
        mTextPaint.textSize = 36.0f
    }

    /**
     * Updates the barcode instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    internal fun updateItem(barcode: Barcode) {
        this.barcode = barcode
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        val barcode = this.barcode ?: return

        // Draws bounding box
        val rect = RectF(barcode.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, mRectPaint)

        // Draw label
        canvas.drawText(barcode.rawValue, rect.left, rect.bottom, mTextPaint)
    }

    companion object {

        private val COLOR_CHOICES = intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN)

        private var mCurrentColorIndex = 0
    }
}
