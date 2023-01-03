package com.fadlurahmanf.starterappmvvm.ui.notification

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.ui.example.activity.CallActivity
import com.fadlurahmanf.starterappmvvm.utils.notification.CallNotificationCallHelper
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationBroadcastReceiver
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationBroadcastReceiver.Companion.ACTION_ACCEPT_CALL
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationBroadcastReceiver.Companion.ACTION_CALL_INCOMING
import com.fadlurahmanf.starterappmvvm.utils.notification.NotificationBroadcastReceiver.Companion.ACTION_DECLINED_CALL

class FullScreenNotification : AppCompatActivity() {
    lateinit var acceptBtn:ImageView
    lateinit var declinedBtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_notification)
        acceptBtn = findViewById<ImageView>(R.id.iv_accept)
        declinedBtn = findViewById<ImageView>(R.id.iv_decline)
        println("masuk sini ${this.intent.extras?.getBundle(NotificationBroadcastReceiver.EXTRA_DATA)?.getInt(CallNotificationCallHelper.EXTRA_NOTIFICATION_ID)}")

        acceptBtn.setOnClickListener {
            NotificationBroadcastReceiver.sendBroadcastAcceptCall(this)
            finish()
        }

        declinedBtn.setOnClickListener {
            NotificationBroadcastReceiver.sendBroadcastDeclinedCall(this)
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }

        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardManager.requestDismissKeyguard(this@FullScreenNotification, null)
        }
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
        super.onDestroy()
    }
}