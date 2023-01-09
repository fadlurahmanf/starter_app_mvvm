package com.fadlurahmanf.starterappmvvm.utils.download

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.R

class DownloadNotificationHelper(var context: Context) {
    companion object{
        const val DOWNLOAD_CHANNEL_ID = "DOWNLOAD_CHANNEL_ID"
        const val DOWNLOAD_CHANNEL = "Download"
        const val DOWNLOAD_CHANNEL_DESCRIPTION = "Download Description"

        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        const val EXTRA_TOTAL_SIZE = "EXTRA_TOTAL_SIZE"
        const val EXTRA_CURRENT_PROGRESS_SIZE = "EXTRA_CURRENT_PROGRESS_SIZE"
    }

    private lateinit var notificationManager: NotificationManager

    init {
        createDownloadChannel()
    }

    private fun createDownloadChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(DOWNLOAD_CHANNEL_ID, DOWNLOAD_CHANNEL, importance).apply {
                description = DOWNLOAD_CHANNEL_DESCRIPTION
                setSound(null, null)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun prepareDownload(): Notification{
        createDownloadChannel()
        val builder = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
            .setSmallIcon(R.drawable.il_logo_bankmas)
            .setContentTitle("Download")
            .setContentText("Download Progress")
            .setProgress(1, 0, false)
            .setPriority(NotificationCompat.PRIORITY_LOW)


        return builder.build()
    }

    fun showDownload(data: Bundle) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createDownloadChannel()
        val progress = data.getInt(EXTRA_CURRENT_PROGRESS_SIZE)
        val total = data.getInt(EXTRA_TOTAL_SIZE)
        val id = data.getInt(EXTRA_NOTIFICATION_ID)
        val builder = NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
            .setSmallIcon(R.drawable.il_logo_bankmas)
            .setContentTitle("tffajari - Download")
            .setContentText("tffajari - Download Progress")

        if (progress > total){
            builder.setProgress(1, 1,false)
        }else{
            builder.setProgress(total, progress, false)
        }

        notificationManager.notify(id, builder.build())
    }
}