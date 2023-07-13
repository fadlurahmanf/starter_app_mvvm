package com.fadlurahmanf.starterappmvvm.feature.qris.external.helper

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.lang.Exception

@ExperimentalGetImage
class QRCodeAnalyzer(
    var qrisListener: QrisListener
) : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val scanner = BarcodeScanning.getClient(options)

            scanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        if (barcode.rawValue != null) {
                            logConsole.d("top: ${barcode.boundingBox?.top}")
                            logConsole.d("bottom: ${barcode.boundingBox?.bottom}")
                            if ((barcode.boundingBox?.top ?: 0) > 75
                                && (barcode.boundingBox?.bottom ?: 0) < 450
                            ) {
                                qrisListener.onSuccessGetQris(barcode.rawValue!!)
                            }
                        }
                    }
                }.addOnFailureListener {
                    qrisListener.onFailedGetQris(it)
                }

            image.close()
        }
    }

    interface QrisListener {
        fun onSuccessGetQris(value: String)
        fun onFailedGetQris(e: Exception)
    }
}