package com.fadlurahmanf.starterappmvvm.feature.mlkit.external.helper

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception

class ImageLabelAnalyzer(
    private val listener: Listener
) : ImageAnalysis.Analyzer {

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            labeler.process(
                InputImage.fromMediaImage(
                    mediaImage,
                    image.imageInfo.rotationDegrees
                )
            ).addOnSuccessListener { labels ->
                listener.onSuccessGetLabels(labels.toList(), image)
            }.addOnFailureListener {
                listener.onFailed(it)
            }
        }
    }

    interface Listener {
        fun onSuccessGetLabels(labels: List<ImageLabel>, image: ImageProxy)
        fun onFailed(e: Exception)
    }
}