package com.fadlurahmanf.starterappmvvm.core.domain.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.external.constant.AppKey

abstract class BaseNotification(val context: Context) {
    abstract val channelId: String
    abstract val channel: String
    abstract val description: String

    init {
        createChannel()
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channel,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description = this@BaseNotification.description
                setSound(null, null)
            }
            val nm =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    fun showNotification(
        id: Int = AppKey.Notification.DEFAULT_NOTIFICATION_ID,
        title: String,
        body: String
    ) {
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.il_logo_bankmas)
            .setContentTitle(title)
            .setContentText(body)
        notificationManager.notify(id, builder.build())
    }
}