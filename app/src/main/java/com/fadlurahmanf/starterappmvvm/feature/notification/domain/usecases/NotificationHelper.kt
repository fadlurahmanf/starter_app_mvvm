package com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.R

@Deprecated("DEPRECATE CHANTE TO NOTIFICATION IMPL")
class NotificationHelper(
    var context: Context
) {
    companion object {
        const val GENERAL_CHANNEL_ID = "GENERAL_CHANNEL_ID"
        const val GENERAL_CHANNEL = "General"
        const val GENERAL_CHANNEL_DESCRIPTION = "General Description"
    }

    var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createGeneralChannel()
    }

    private fun createGeneralChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel =
                NotificationChannel(GENERAL_CHANNEL_ID, GENERAL_CHANNEL, importance).apply {
                    description = GENERAL_CHANNEL_DESCRIPTION
                }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    val builder = NotificationCompat.Builder(context, GENERAL_CHANNEL_ID)
        .setSmallIcon(R.drawable.il_logo_bankmas)

    fun showNotification(id: Int, title: String, body: String) {
        builder.setContentTitle(title)
            .setContentText(body)

        notificationManager.notify(id, builder.build())
    }
}