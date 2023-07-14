package com.fadlurahmanf.starterappmvvm.feature.mlkit.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.core.camera.domain.common.BaseCameraActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleFaceDetectorBinding
import com.fadlurahmanf.starterappmvvm.feature.mlkit.external.helper.FaceDetectorAnalyzer
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.google.mlkit.vision.face.Face
import java.util.concurrent.Executors

/**
 * references [https://developers.google.com/ml-kit/vision/face-detection/android?hl=id]
 * */
class ExampleFaceDetectorActivity :
    BaseCameraActivity<ActivityExampleFaceDetectorBinding>(ActivityExampleFaceDetectorBinding::inflate) {
    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
    }

    private lateinit var image: ImageProxy
    private lateinit var faces: List<Face>
    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            faces.forEach {
                logConsole.d("ON SUCCESS FACE DETECTOR:")
                logConsole.d("SMILING PROBABILITY: ${it.smilingProbability}")
                logConsole.d("LEFT EYE OPEN PROBABILITY: ${it.leftEyeOpenProbability}")
                logConsole.d("RIGHT EYE OPEN PROBABILITY: ${it.rightEyeOpenProbability}")
            }
            handler.postDelayed(this, 3000)
            image.close()
        }

    }

    private val listener = object : FaceDetectorAnalyzer.Listener {

        override fun onSuccessGetFaces(labels: List<Face>, image: ImageProxy) {
            this@ExampleFaceDetectorActivity.image = image
            this@ExampleFaceDetectorActivity.faces = labels
            handler.removeCallbacks(onSuccessRunnable)
            handler.postDelayed(onSuccessRunnable, 3000)
        }

        override fun onFailedGetFaces(e: java.lang.Exception) {
            logConsole.e("ERROR GET LABELS: ${e.message}")
            println("MASUK ERROR ${e.message}")
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
                setAnalyzer(cameraExecutor, FaceDetectorAnalyzer(listener))
            }
        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
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