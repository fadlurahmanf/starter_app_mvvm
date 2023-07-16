package com.fadlurahmanf.starterappmvvm.feature.qris.external.helper

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class QRCodeAnalyzer(
    var listener: Listener
) : ImageAnalysis.Analyzer {

    @ExperimentalGetImage
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
                    listener.onSuccessGetQris(barcodes, image)
                }.addOnFailureListener {
                    listener.onFailedGetQris(it)
                }
        }
    }

    interface Listener {
        fun onSuccessGetQris(value: List<Barcode>, image: ImageProxy)
        fun onFailedGetQris(e: Exception)
    }
}