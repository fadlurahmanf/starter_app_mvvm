package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import android.content.Intent
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.AfterLoginViewModel
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.BaseAfterLoginViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.event.RxEvent
import com.fadlurahmanf.starterappmvvm.core.unknown.external.helper.RxBus
import com.fadlurahmanf.starterappmvvm.unknown.ui.SplashActivity
import javax.inject.Inject

abstract class BaseAfterLoginActivity<VB : ViewBinding>(inflate: InflateActivity<VB>) :
    BaseActivity<VB>(inflate) {

    abstract fun inject1()

    override fun onResume() {
        super.onResume()
        timer.cancel()
        timer.start()
    }

    private fun restartTimer() {
        timer.cancel()
        currentMillis = defaultMillis
        timer.start()
    }

    override fun listenEvent() {
        super.listenEvent()
        compositeDisposable.add(RxBus.listen(RxEvent.ForceLogoutEvent::class.java).subscribe {
            logConsole.d("EVENT FORCE LOGOUT")
            compositeDisposable.clear()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (event?.action == MotionEvent.ACTION_DOWN) {
            if (currentMillis < 5000L) {
                restartTimer()
            }
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}

private val defaultMillis: Long = 10000L
private var currentMillis: Long = 10000L

private val timer = object : CountDownTimer(currentMillis, 1000) {
    override fun onTick(millisUntilFinished: Long) {
        currentMillis = millisUntilFinished
        logConsole.d("TICK UNTIL FINISIHED: $millisUntilFinished")
    }

    override fun onFinish() {
        RxBus.publish(RxEvent.ForceLogoutEvent())
        this.cancel()
    }
}