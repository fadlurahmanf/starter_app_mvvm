package com.fadlurahmanf.starterappmvvm.unknown.ui.notification

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
import com.fadlurahmanf.starterappmvvm.unknown.utils.call.CallBroadcastReceiver

class FullScreenNotificationActivity : AppCompatActivity() {
    lateinit var acceptBtn:ImageView
    lateinit var declinedBtn:ImageView
    companion object {
        const val ACTION_ENDED_CALL = "com.fadlurahmanf.callkit.ACTION_ENDED_CALL"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_notification)
        acceptBtn = findViewById<ImageView>(R.id.iv_accept)
        declinedBtn = findViewById<ImageView>(R.id.iv_decline)
        registerReceiver(receiver, IntentFilter(ACTION_ENDED_CALL))

        acceptBtn.setOnClickListener {
            CallBroadcastReceiver.sendBroadcastAcceptCall(this)
            finish()
        }

        declinedBtn.setOnClickListener {
            CallBroadcastReceiver.sendBroadcastDeclinedCall(this)
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
            keyguardManager.requestDismissKeyguard(this@FullScreenNotificationActivity, null)
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                ACTION_ENDED_CALL -> {
                    finishAndRemoveTask()
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
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