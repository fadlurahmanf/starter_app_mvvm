package com.fadlurahmanf.starterappmvvm.core.domain.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.ExampleEncryptDecryptActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.LoginActivity

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        // todo: register action inside manifest
        const val ACTION_CLICK = "com.fadlurahmanf.notification.CLICK"

        const val ADDITIONAL_DATA = "ADDITIONAL_DATA"

        fun getClickPendingIntent(
            context: Context,
            notificationId: Int,
            data: Bundle? = null
        ): PendingIntent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.apply {
                action = ACTION_CLICK
                data?.let { p0Data ->
                    putExtra(ADDITIONAL_DATA, p0Data)
                }
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

    private fun getAdditionalData(intent: Intent): Bundle? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(ADDITIONAL_DATA, Bundle::class.java)
        } else {
            intent.extras?.getParcelable(ADDITIONAL_DATA)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_CLICK -> {
                val additionalData = getAdditionalData(intent)
                additionalData?.let { data ->
                    onClickIntentAction(context, data)
                }
            }
        }
    }

    private fun onClickIntentAction(context: Context?, data: Bundle) {
        if (data.getString("TYPE") == "DOWNLOAD") {
            val newIntent = Intent(context, ExampleEncryptDecryptActivity::class.java)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(newIntent)
        } else {
            val newIntent = Intent(context, LoginActivity::class.java)
            newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(newIntent)
        }
    }
}