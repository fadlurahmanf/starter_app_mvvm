package com.fadlurahmanf.starterappmvvm.feature.download.domain.usecases

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseNotification
import com.fadlurahmanf.starterappmvvm.core.domain.receiver.NotificationReceiver

class DownloadNotificationImpl(context: Context) : BaseNotification(context) {
    companion object {
        const val DOWNLOAD_CHANNEL_ID = "DOWNLOAD_CHANNEL_ID"
        const val DOWNLOAD_CHANNEL = "Download"
        const val DOWNLOAD_CHANNEL_DESCRIPTION = "Download Description"

        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        const val EXTRA_TOTAL_SIZE = "EXTRA_TOTAL_SIZE"
        const val EXTRA_CURRENT_PROGRESS_SIZE = "EXTRA_CURRENT_PROGRESS_SIZE"
    }

    override val channelId: String
        get() = AppKey.Notification.DOWNLOAD_CHANNEL_ID
    override val channel: String
        get() = AppKey.Notification.DOWNLOAD_CHANNEL
    override val description: String
        get() = AppKey.Notification.DOWNLOAD_DESCRIPTION

    override var defaultNotificationId: Int = AppKey.Notification.DEFAULT_DOWNLOAD_NOTIFICATION_ID

    private fun prepareDownloadNotificationBuilder(): NotificationCompat.Builder {
        val bundle = Bundle().apply {
            putString("TYPE", "DOWNLOAD")
        }
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.il_logo_bankmas)
            .setContentTitle("DOWNLOAD")
            .setContentText("Start downloading")
            .setContentIntent(
                NotificationReceiver.getClickPendingIntent(
                    context,
                    defaultNotificationId,
                    bundle
                )
            )
    }

    fun showPrepareDownload() {
        showNotification(defaultNotificationId, prepareDownloadNotificationBuilder())
    }

//    fun showDownload(data: Bundle) {
//        notificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        createDownloadChannel()
//        val progress = data.getInt(EXTRA_CURRENT_PROGRESS_SIZE)
//        val total = data.getInt(EXTRA_TOTAL_SIZE)
//        val id = data.getInt(EXTRA_NOTIFICATION_ID)
//        val builder = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
//            .setSmallIcon(R.drawable.il_logo_bankmas)
//            .setContentTitle("tffajari - Download")
//            .setContentText("tffajari - Download Progress")
//
//        if (progress > total) {
//            builder.setProgress(1, 1, false)
//        } else {
//            builder.setProgress(total, progress, false)
//        }
//
//        notificationManager.notify(id, builder.build())
//    }
}