package com.example.jesse.qrscanner

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView

import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode

class MainActivity : Activity(), View.OnClickListener {

    lateinit var autoFocus: CompoundButton
    lateinit var useFlash: CompoundButton
    lateinit var statusMessage: TextView
    lateinit var barcodeValue: TextView

    private val RC_BARCODE_CAPTURE = 9001
    private val TAG = "BarcodeMain"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusMessage = findViewById(R.id.status_message)
        barcodeValue = findViewById(R.id.barcode_value)
        autoFocus = findViewById(R.id.auto_focus)
        useFlash = findViewById(R.id.use_flash)

        findViewById<View>(R.id.read_barcode).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if(v.id == R.id.read_barcode) {
            val intent = Intent(this, BarcodeCaptureActivity::class.java)
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)

            startActivityForResult(intent, RC_BARCODE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    statusMessage.setText(R.string.barcode_success)
                    barcodeValue.setText(barcode.displayValue)
                    Log.d(TAG, "Barcode read: " + barcode.displayValue)
                } else {
                    statusMessage.setText(R.string.barcode_failure)
                    Log.d(TAG, "No barcode captured, intent data is null")
                }
            } else {
                statusMessage.text = String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
