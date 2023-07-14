package com.fadlurahmanf.starterappmvvm.feature.mlkit.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleImageLabelingBinding
import com.fadlurahmanf.starterappmvvm.feature.mlkit.external.helper.ImageLabelAnalyzer
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.google.mlkit.vision.label.ImageLabel
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ExampleImageLabelingActivity :
    BaseActivity<ActivityExampleImageLabelingBinding>(ActivityExampleImageLabelingBinding::inflate) {

    private lateinit var cameraExecutor: ExecutorService

    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
        binding.btnRestart.setOnClickListener {
            analyze()
        }
    }

    private val listener = object : ImageLabelAnalyzer.Listener {
        override fun onSuccessGetLabels(labels: List<ImageLabel>, image: ImageProxy) {
            val isContainObject = labels.firstOrNull()?.text?.contains("Mobile phone")
            if (isContainObject == true) {
                val detectedObject = labels.first {
                    it.text.contains("Mobile phone")
                }
                println("MASUK SUCCESS ${detectedObject.text} DAN ${detectedObject.confidence}")
            } else {
                image.close()
            }
        }

        override fun onFailed(e: Exception) {
            println("MASUK ERROR ${e.message}")
        }

    }

    private fun initCameraListener() {
        cameraProviderFuture().addListener({
            analyze()
        }, ContextCompat.getMainExecutor(this))
    }

    private lateinit var analyzer: ImageAnalysis

    private fun analyze() {
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

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun cameraProviderFuture() = ProcessCameraProvider.getInstance(this)

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}