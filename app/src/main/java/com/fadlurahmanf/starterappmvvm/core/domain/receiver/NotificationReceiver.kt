package com.fadlurahmanf.starterappmvvm.core.domain.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.LoginActivity

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_CLICK = "com.fadlurahmanf.notification.CLICK"

        fun getClickPendingIntent(context: Context, notificationId: Int): PendingIntent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.apply {
                action = ACTION_CLICK
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        private fun getFlagPendingIntent(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_CLICK -> {
                onClickIntentAction(context, intent)
            }
        }
    }

    private fun onClickIntentAction(context: Context?, intent: Intent) {
        val newIntent = Intent(context, LoginActivity::class.java)
        newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(newIntent)
    }
}