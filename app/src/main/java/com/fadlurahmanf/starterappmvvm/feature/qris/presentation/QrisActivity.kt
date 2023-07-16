package com.fadlurahmanf.starterappmvvm.feature.qris.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import com.fadlurahmanf.starterappmvvm.core.camera.domain.common.BaseCameraActivity
import com.fadlurahmanf.starterappmvvm.core.network.presentation.ExampleAfterLogin2Activity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.databinding.ActivityQrisBinding
import com.fadlurahmanf.starterappmvvm.feature.qris.external.helper.QRCodeAnalyzer
import com.fadlurahmanf.starterappmvvm.feature.qris.presentation.viewmodel.QrisViewModel
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import java.util.concurrent.Executors
import javax.inject.Inject

class QrisActivity : BaseCameraActivity<ActivityQrisBinding>(ActivityQrisBinding::inflate) {

    @Inject
    lateinit var viewModel: QrisViewModel

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uriNull: Uri? ->
            uriNull?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                scanQRImage(bitmap)
            }
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
                    showSnackBar(binding.root, "SUCCESS INQUIRY QRIS")
                    analyzer.setAnalyzer(
                        ContextCompat.getMainExecutor(this),
                        QRCodeAnalyzer(listener)
                    )
                }

                is CustomState.Error -> {
                    dismissLoadingDialog()
                    logConsole.e("ERROR INQUIRY QRIS ${it.exception}")
                    showSnackBar(binding.root, "ERROR INQUIRY QRIS")
                    analyzer.setAnalyzer(
                        ContextCompat.getMainExecutor(this),
                        QRCodeAnalyzer(listener)
                    )
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

    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        initCameraListener()
        initObserver()
        initAction()
    }

    private lateinit var image: ImageProxy
    private lateinit var barcode: List<Barcode>
    private val onSuccessRunnable = object : Runnable {
        override fun run() {
            var rawBarcodeValue: String? = null
            for (i in this@QrisActivity.barcode.indices) {
                if (this@QrisActivity.barcode[i].rawValue != null) {
                    rawBarcodeValue = this@QrisActivity.barcode[i].rawValue
                }
            }
            if (rawBarcodeValue != null) {
                logConsole.d("MASUK $rawBarcodeValue")
                analyzer.clearAnalyzer()
                this@QrisActivity.image.close()
                viewModel.inquiryQris(rawBarcodeValue)
            } else {
                logConsole.d("RAW BARCODE NULL")
                handler.postDelayed(this, 3000L)
                this@QrisActivity.image.close()
            }
        }
    }

    private val listener = object : QRCodeAnalyzer.Listener {

        override fun onSuccessGetQris(value: List<Barcode>, image: ImageProxy) {
            this@QrisActivity.barcode = value
            this@QrisActivity.image = image
            handler.removeCallbacks(onSuccessRunnable)
            handler.post(onSuccessRunnable)
        }

        override fun onFailedGetQris(e: Exception) {
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
                setAnalyzer(cameraExecutor, QRCodeAnalyzer(listener))
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