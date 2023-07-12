package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import android.content.Intent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityQrisBinding
import com.fadlurahmanf.starterappmvvm.unknown.utils.qrcode.QRCodeAnalyzer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@androidx.camera.core.ExperimentalGetImage
class QrisActivity : BaseActivity<ActivityQrisBinding>(ActivityQrisBinding::inflate) {

    private lateinit var cameraExecutor: ExecutorService

    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }

    private var isSuccessGetQris = false

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val qrisListener = object : QRCodeAnalyzer.QrisListener{
            override fun onSuccessGetQris(value: String) {
                if (!isSuccessGetQris){
                    isSuccessGetQris = true
                    val intent = Intent()
                    intent.putExtra("RESULT", value)
                    setResult(RESULT_OK, intent)
                }
            }

            override fun onFailedGetQris(exception: java.lang.Exception) {

            }
        }


        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer(qrisListener))
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }


        }, ContextCompat.getMainExecutor(this))
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun inject() {

    }

}