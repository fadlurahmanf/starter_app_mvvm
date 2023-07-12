package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.AfterLoginViewModel
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.BaseAfterLoginViewModel
import javax.inject.Inject

abstract class BaseAfterLoginActivity<VB : ViewBinding>(inflate: InflateActivity<VB>) :
    BaseActivity<VB>(inflate) {

   abstract fun inject1()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}