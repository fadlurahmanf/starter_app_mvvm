package com.fadlurahmanf.starterappmvvm.feature.mlkit.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.core.camera.domain.common.BaseCameraActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleImageLabelingBinding
import com.fadlurahmanf.starterappmvvm.feature.mlkit.external.helper.ImageLabelAnalyzer
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.google.mlkit.vision.label.ImageLabel
import java.lang.Exception
import java.util.concurrent.Executors

/**
 * references: https://developers.google.com/ml-kit/vision/image-labeling/android
 * */
class ExampleImageLabelingActivity :
    BaseCameraActivity<ActivityExampleImageLabelingBinding>(ActivityExampleImageLabelingBinding::inflate) {

    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
    }

    private lateinit var image: ImageProxy
    private lateinit var labels: List<ImageLabel>
    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            labels.forEach {
                logConsole.d("ON SUCCESS IMAGE LABELING ${it.text} DAN ${it.confidence}")
            }
            handler.postDelayed(this, 3000)
            image.close()
        }

    }

    private val listener = object : ImageLabelAnalyzer.Listener {
        override fun onSuccessGetLabels(labels: List<ImageLabel>, image: ImageProxy) {
            this@ExampleImageLabelingActivity.image = image
            this@ExampleImageLabelingActivity.labels = labels
            handler.removeCallbacks(onSuccessRunnable)
            handler.postDelayed(onSuccessRunnable, 3000)
        }

        override fun onFailedGetLabels(e: Exception) {
            logConsole.e("ERROR GET LABELS: ${e.message}")
        }

    }

    override fun initCameraListener() {
        cameraProviderFuture().addListener({
            analyze()
        }, ContextCompat.getMainExecutor(this))
    }

    private lateinit var analyzer: ImageAnalysis

    override fun analyze() {
        val cameraProvider = cameraProviderFuture().get()
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(binding.cameraView.surfaceProvider)
        }


        analyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build().apply {
                setAnalyzer(cameraExecutor, ImageLabelAnalyzer(listener))
            }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, analyzer
            )
        } catch (e: Throwable) {
            logConsole.e("ERROR LISTEN CAMERA: ${e.message}")
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}