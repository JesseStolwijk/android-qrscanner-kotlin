package com.example.jesse.qrscanner

import android.content.Context
import android.support.annotation.UiThread
import com.example.jesse.qrscanner.ui.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

class BarcodeGraphicTracker(private val mOverlay: GraphicOverlay<BarcodeGraphic>,
                            private var mGraphic: BarcodeGraphic,
                            context: Context) : Tracker<Barcode>()  {

    private val mBarcodeUpdateListener: BarcodeUpdateListener

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

    override fun onNewItem(id: Int, item: Barcode) {
        mGraphic.id = id
        mBarcodeUpdateListener.onBarcodeDetected(item)
    }

    override fun onUpdate(detectionResults: Detector.Detections<Barcode>, item: Barcode) {
        mOverlay.add(mGraphic)
        mGraphic.updateItem(item)
    }

    override fun onMissing(p0: Detector.Detections<Barcode>?) {
        mOverlay.remove(mGraphic)
    }

    override fun onDone() {
        mOverlay.remove(mGraphic)
    }
}