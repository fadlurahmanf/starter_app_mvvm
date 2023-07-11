package com.fadlurahmanf.starterappmvvm.feature.download.external.helper

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.common.BaseNotification
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.receiver.NotificationReceiver
import com.fadlurahmanf.starterappmvvm.feature.notification.data.constant.NotificationConstant
import com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model.ContentNotificationModel

class DownloadNotificationHelper (context: Context) : BaseNotification(context) {

    override val channelId: String
        get() = NotificationConstant.DOWNLOAD_CHANNEL_ID
    override val channel: String
        get() = NotificationConstant.DOWNLOAD_CHANNEL
    override val description: String
        get() = NotificationConstant.DOWNLOAD_DESCRIPTION

    override var defaultNotificationId: Int = NotificationConstant.DEFAULT_DOWNLOAD_NOTIFICATION_ID

    companion object {
        const val EXTRA_CURRENT_PROGRESS_SIZE = "EXTRA_CURRENT_PROGRESS_SIZE"
        const val EXTRA_TOTAL_SIZE = "EXTRA_TOTAL_SIZE"
    }

    private fun getClickPendingIntent(): PendingIntent {
        val content = ContentNotificationModel(
            type = "DOWNLOAD"
        )
        return NotificationReceiver.getClickPendingIntent(
            context,
            defaultNotificationId,
            content
        )
    }

    fun notificationPrepareDownload(): NotificationCompat.Builder {
        return notificationBuilder(defaultNotificationId, "DOWNLOAD", "Start downloading")
            .setContentIntent(getClickPendingIntent())
    }

    fun showPrepareDownload() {
        showNotification(defaultNotificationId, notificationPrepareDownload())
    }

    fun showDownload(data: Bundle) {
        val progress = data.getInt(EXTRA_CURRENT_PROGRESS_SIZE)
        val total = data.getInt(EXTRA_TOTAL_SIZE)
        val builder = notificationBuilder(
            defaultNotificationId,
            "DOWNLOAD",
            "Downloading ${(progress.toDouble() / total.toDouble()) * 100.0}%"
        )

        if (progress > total) {
            builder.setProgress(1, 1, false)
        } else {
            builder.setProgress(total, progress, false)
        }

        showNotification(defaultNotificationId, builder)
    }
}