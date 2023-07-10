package com.fadlurahmanf.starterappmvvm.feature.notification.domain.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model.ContentNotificationModel
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.LoginActivity

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        // todo: register action inside manifest
        const val ACTION_NOTIFICATION_CLICK = "com.fadlurahmanf.notification.NOTIFICATION_CLICK"
        const val ACTION_CLICK = "com.fadlurahmanf.notification.ACTION_CLICK"

        const val CONTENT_DATA = "CONTENT_DATA"

        fun getClickPendingIntent(
            context: Context,
            notificationId: Int,
            data: ContentNotificationModel? = null
        ): PendingIntent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_CLICK
                data?.let { p0Data ->
                    putExtra(CONTENT_DATA, p0Data)
                }
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        fun getActionClickPendingIntent(
            context: Context,
            notificationId: Int,
            data: ContentNotificationModel? = null
        ): PendingIntent {
            val intent = Intent(context, NotificationReceiver::class.java)
            intent.apply {
                action = ACTION_CLICK
                data?.let { p0Data ->
                    putExtra(CONTENT_DATA, p0Data)
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

    private fun getContentData(intent: Intent): ContentNotificationModel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(CONTENT_DATA, ContentNotificationModel::class.java)
        } else {
            intent.extras?.getParcelable(CONTENT_DATA)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ACTION_NOTIFICATION_CLICK -> {
                val contentData = getContentData(intent)
                contentData?.let { data ->
                    onClickIntentAction(context, data)
                }
            }

            ACTION_CLICK -> {
                val contentData = getContentData(intent)
                contentData?.let { data ->
                    onClickIntentAction(context, data)
                }
            }
        }
    }

    private fun onClickIntentAction(context: Context?, data: ContentNotificationModel) {
        when (data.type) {
            "DOWNLOAD" -> {
                logConsole.d("ON CLICK INTENT ACTION DOWNLOAD")
            }

            "SNOOZE" -> {
                logConsole.d("ON CLICK INTENT ACTION SNOOZE")
            }

            // TODO: ADD TYPE
            else -> {
                val newIntent = Intent(context, LoginActivity::class.java)
                newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(newIntent)
            }
        }
    }
}