package com.example.jesse.qrscanner

import android.content.Context
import android.support.annotation.UiThread
import com.example.jesse.qrscanner.ui.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeGraphicTracker(private val mOverlay: GraphicOverlay<BarcodeGraphic>,
                            private var mGraphic: BarcodeGraphic,
                            private val context: Context) : Tracker<Barcode>()  {

    private lateinit var mBarcodeUpdateListener: BarcodeUpdateListener

    init {
        if (context is BarcodeUpdateListener) {
            this.mBarcodeUpdateListener = context
        } else {
            throw RuntimeException("Hosting activity must implement BarcodeUpdateListener")
        }
    }

    interface BarcodeUpdateListener {
        @UiThread
        fun onBarcodeDetected(barcode: Barcode)
    }

    /**
     * Start tracking the detected item instance within the item overlay.
     */
    override fun onNewItem(id: Int, item: Barcode) {
        mGraphic.id = id
        mBarcodeUpdateListener.onBarcodeDetected(item)
    }

    /**
     * Update the position/characteristics of the item within the overlay.
     */
    override fun onUpdate(detectionResults: Detector.Detections<Barcode>, item: Barcode) {
        mOverlay.add(mGraphic)
        mGraphic.updateItem(item)
    }

    /**
     * Hide the graphic when the corresponding object was not detected.  This can happen for
     * intermediate frames temporarily, for example if the object was momentarily blocked from
     * view.
     */
    override fun onMissing(p0: Detector.Detections<Barcode>?) {
        mOverlay.remove(mGraphic)
    }

    /**
     * Called when the item is assumed to be gone for good. Remove the graphic annotation from
     * the overlay.
     */
    override fun onDone() {
        mOverlay.remove(mGraphic)
    }
}