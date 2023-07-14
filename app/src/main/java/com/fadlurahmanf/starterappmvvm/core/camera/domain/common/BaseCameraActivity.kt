package com.fadlurahmanf.starterappmvvm.core.camera.domain.common

import android.os.Handler
import android.os.Looper
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.InflateActivity
import java.util.concurrent.ExecutorService

abstract class BaseCameraActivity<VB : ViewBinding>(inflate: InflateActivity<VB>) :
    BaseActivity<VB>(inflate) {
    lateinit var cameraExecutor: ExecutorService

    val handler = Handler(Looper.getMainLooper())

    abstract fun initCameraListener()

    abstract fun analyze()

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    open fun cameraProviderFuture() = ProcessCameraProvider.getInstance(this)

}