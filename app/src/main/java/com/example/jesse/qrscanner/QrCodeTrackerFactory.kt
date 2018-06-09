package com.example.jesse.qrscanner

import android.content.Context
import com.example.jesse.qrscanner.ui.camera.GraphicOverlay
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode

internal class QrCodeTrackerFactory(private val mGraphicOverlay: GraphicOverlay<QrCodeGraphic>,
                                    private val mContext: Context) : MultiProcessor.Factory<Barcode> {

    override fun create(barcode: Barcode): Tracker<Barcode> {
        val graphic = QrCodeGraphic(mGraphicOverlay)
        return QrCodeGraphicTracker(mGraphicOverlay, graphic, mContext)
    }
}