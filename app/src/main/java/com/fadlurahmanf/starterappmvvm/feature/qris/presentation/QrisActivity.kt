package com.fadlurahmanf.starterappmvvm.feature.qris.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.core.network.presentation.ExampleAfterLogin2Activity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityQrisBinding
import com.fadlurahmanf.starterappmvvm.feature.qris.external.helper.QRCodeAnalyzer
import com.fadlurahmanf.starterappmvvm.feature.qris.presentation.viewmodel.QrisViewModel
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@androidx.camera.core.ExperimentalGetImage
class QrisActivity : BaseActivity<ActivityQrisBinding>(ActivityQrisBinding::inflate) {

    private lateinit var cameraExecutor: ExecutorService

    @Inject
    lateinit var viewModel: QrisViewModel

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uriNull: Uri? ->
            uriNull?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                scanQRImage(bitmap)
            }
        }

    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
        initObserver()
        initAction()
    }

    private fun initObserver() {
        viewModel.inquiryState.observe(this) {
            when (it) {
                is CustomState.Loading -> {
                    showLoadingDialog()
                }

                is CustomState.Success -> {
                    dismissLoadingDialog()
                    logConsole.d("SUCCESS INQUIRY: ${it.data}")
                    val intent = Intent(this, ExampleAfterLogin2Activity::class.java)
                    startActivity(intent)
                }

                is CustomState.Error -> {
                    dismissLoadingDialog()
                    logConsole.e("ERROR INQUIRY QRIS ${it.exception}")
                    showSnackBar(binding.root, "ERROR INQUIRY QRIS")
                }

                else -> {
                    dismissLoadingDialog()
                }
            }
        }
    }

    private fun initAction() {
        binding.btnGallery.setOnClickListener {
            pickImageResult.launch("image/*")
        }
    }

    private var isSuccessGetQris = false

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val qrisListener = object : QRCodeAnalyzer.QrisListener {
            override fun onSuccessGetQris(value: String) {
                if (!isSuccessGetQris) {
                    isSuccessGetQris = true
                    logConsole.d("MASUK GET QRIS $value")
                    viewModel.inquiryQris(value)
                }
            }

            override fun onFailedGetQris(e: Exception) {
//                showSnackBar(binding.root, "ERROR GET QRIS: $e")
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

    private fun scanQRImage(bMap: Bitmap) {
        val intArray = IntArray(bMap.width * bMap.height)
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
        val source: LuminanceSource = RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(bitmap)
            viewModel.inquiryQris(result.text)
        } catch (e: Exception) {
            logConsole.e("ERROR SCAN QRIS FROM IMAGE: ${e.message}")
            showSnackBar(binding.root, "ERROR SCAN QRIS FROM IMAGE: ${e.message}")
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}