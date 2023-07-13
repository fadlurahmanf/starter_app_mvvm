package com.fadlurahmanf.starterappmvvm.feature.qris.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.presentation.ImageViewerActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityQrisBinding
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.constant.ImageOrigin
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.ImageModel
import com.fadlurahmanf.starterappmvvm.feature.qris.external.helper.QRCodeAnalyzer
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.Result
import com.google.zxing.common.HybridBinarizer
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@androidx.camera.core.ExperimentalGetImage
class QrisActivity : BaseActivity<ActivityQrisBinding>(ActivityQrisBinding::inflate) {

    private lateinit var cameraExecutor: ExecutorService

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uriNull: Uri? ->
            uriNull?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                scanQRImage(bitmap)
            }
        }

    private fun convertMediaUriToPath(uri: Uri): String? {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        val columnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
        if (columnIndex != null) {
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close()
            return path
        }
        return null
    }

    override fun initSetup() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
        binding.btnGallery.setOnClickListener {
            pickImageResult.launch("image/*")
        }
    }

    private var isSuccessGetQris = false

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val qrisListener = object : QRCodeAnalyzer.QrisListener {
            override fun onSuccessGetQris(value: String) {
                logConsole.d("value: $value")
                showSnackBar(binding.root, "SUCCESS NIH")
                if (!isSuccessGetQris) {
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

    private fun scanImageQRCode(file: File) {
        val inputStream: InputStream = BufferedInputStream(FileInputStream(file))
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val decoded = scanQRImage(bitmap)
        logConsole.d("Decoded string=$decoded")
    }

    private fun scanQRImage(bMap: Bitmap): String? {
        var contents: String? = null
        val intArray = IntArray(bMap.width * bMap.height)
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
        val source: LuminanceSource = RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = MultiFormatReader()
        try {
            val result: Result = reader.decode(bitmap)
            contents = result.text
            logConsole.d("HASIL: $contents")
            showSnackBar(binding.root, "SUCCESS NIHH")
        } catch (e: Exception) {
            logConsole.e("Error decoding qr code")
            showSnackBar(binding.root, "ERROR: ${e.message}")
        }
        return contents
    }

    override fun inject() {

    }

}