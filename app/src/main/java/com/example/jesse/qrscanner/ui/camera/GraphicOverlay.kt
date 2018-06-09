package com.example.jesse.qrscanner.ui.camera

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.google.android.gms.vision.CameraSource
import java.util.*


class GraphicOverlay<T : GraphicOverlay.Graphic>(context: Context, attrs: AttributeSet) : View(context, attrs) {
    val mLock = Object()
    var mPreviewWidth : Int = 0
    var mWidthScaleFactor = 1.0f
    var mPreviewHeight : Int = 0
    var mHeightScaleFactor = 1.0f
    var mFacing = CameraSource.CAMERA_FACING_BACK
    val mGraphics = mutableSetOf<T>()




    abstract class Graphic(private val overlay: GraphicOverlay<Graphic>) {
        abstract fun draw(canvas: Canvas)
        fun scaleX(horizontal: Float) = horizontal * overlay.mWidthScaleFactor
        fun scaleY(vertical: Float) = vertical * overlay.mHeightScaleFactor

        fun translateX(x: Float): Float {
            return if (overlay.mFacing == CameraSource.CAMERA_FACING_FRONT) {
                overlay.width - scaleX(x)
            } else {
                scaleX(x)
            }
        }

        fun translateY(y: Float) = scaleY(y)

        fun postInvalidate() = overlay.postInvalidate()
    }


    fun clear() {
        synchronized(mLock) {
            mGraphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic: T) {
        synchronized(mLock) {
            mGraphics.add(graphic)
        }
        postInvalidate()
    }

    fun remove(graphic: T) {
        synchronized(mLock) {
            mGraphics.remove(graphic)
        }
        postInvalidate()
    }

    fun getGraphics(): List<T> {
        synchronized(mLock) {
            return Vector(mGraphics)
        }
    }

    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(mLock) {
            mPreviewWidth = previewWidth
            mPreviewHeight = previewHeight
            mFacing = facing
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        synchronized(mLock) {
            if (mPreviewWidth != 0 && mPreviewHeight != 0) {
                mWidthScaleFactor = canvas.width.toFloat() / mPreviewWidth.toFloat()
                mHeightScaleFactor = canvas.height.toFloat() / mPreviewHeight.toFloat()
            }

            for (graphic in mGraphics) {
                graphic.draw(canvas)
            }
        }
    }
}
