package com.fadlurahmanf.starterappmvvm.feature.notification.domain.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.domain.receiver.NotificationReceiver
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey
import com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model.NotificationActionModel
import kotlin.random.Random

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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(NotificationReceiver.getClickPendingIntent(context, id))
    }

    open fun showNotification(
        id: Int = defaultNotificationId,
        title: String,
        body: String
    ) {
        notificationManager().notify(
            id,
            notificationBuilder(id, title, body).build()
        )
    }

    open fun showNotification(
        id: Int = defaultNotificationId,
        builder: NotificationCompat.Builder
    ) {
        notificationManager().notify(
            id,
            builder.build()
        )
    }

    fun showImageNotification(
        id: Int = defaultNotificationId,
        title: String,
        body: String,
        imageUrl: String
    ) {
        val builder = notificationBuilder(id, title, body)
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    builder.setLargeIcon(resource)
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    notificationManager()
                        .notify(id, builder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    }

    fun showActionNotification(
        id: Int = defaultNotificationId,
        title: String,
        body: String,
        actions: List<NotificationActionModel>
    ) {
        val builder = notificationBuilder(id, title, body)
        actions.forEach {
            builder.addAction(it.icon, it.title, it.pendingIntent)
        }
        notificationManager().notify(id, builder.build())
    }
}