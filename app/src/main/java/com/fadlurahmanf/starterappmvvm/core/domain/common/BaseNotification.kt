package com.fadlurahmanf.starterappmvvm.core.domain.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.domain.receiver.NotificationReceiver
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey

abstract class BaseNotification(var context: Context) {
    abstract val channelId: String
    abstract val channel: String
    abstract val description: String

    open var defaultNotificationId: Int = AppKey.Notification.DEFAULT_NOTIFICATION_ID

    init {
        createChannel()
    }

    private fun notificationManager() =
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

    open fun notificationBuilder(
        id: Int,
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.il_logo_bankmas)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(NotificationReceiver.getClickPendingIntent(context, id))
    }

    open fun showNotification(
        id: Int? = null,
        title: String,
        body: String
    ) {
        notificationManager().notify(
            id ?: defaultNotificationId,
            notificationBuilder(id ?: defaultNotificationId, title, body).build()
        )
    }

    open fun showNotification(
        id: Int? = null,
        builder: NotificationCompat.Builder
    ) {
        notificationManager().notify(
            id ?: defaultNotificationId,
            builder.build()
        )
    }
}