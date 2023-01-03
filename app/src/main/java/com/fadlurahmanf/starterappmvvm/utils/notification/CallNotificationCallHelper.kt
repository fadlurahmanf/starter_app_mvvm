package com.fadlurahmanf.starterappmvvm.utils.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.ui.notification.FullScreenNotification

class CallNotificationCallHelper(var context: Context) {
    companion object{
        const val CALL_CHANNEL_ID = "CALL_CHANNEL_ID"
        const val CALL_CHANNEL = "Call"
        const val CALL_CHANNEL_DESCRIPTION = "Call Description"

        const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        const val EXTRA_CALLER_NAME = "EXTRA_CALLER_NAME"
    }

    init {
        createCallChannel()
    }

    private fun createCallChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CALL_CHANNEL_ID, CALL_CHANNEL, importance).apply {
                description = CALL_CHANNEL_DESCRIPTION
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private var notificationView:RemoteViews? = null
    private var smallNotificationView: RemoteViews? = null
    fun showIncomingCallNotification(data: Bundle){
        createCallChannel()
        val notificationId = data.getInt(EXTRA_NOTIFICATION_ID)
        val builder = NotificationCompat.Builder(context, CALL_CHANNEL_ID)
            .setSmallIcon(R.drawable.il_logo_bankmas)
        builder.setCategory(NotificationCompat.CATEGORY_CALL)
        builder.priority = NotificationCompat.PRIORITY_MAX
        builder.setAutoCancel(true)
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        builder.setOngoing(true)
        builder.setWhen(0)
        builder.setTimeoutAfter(0L)
        builder.setOnlyAlertOnce(true)
        builder.setFullScreenIntent(getFullScreenIntent(notificationId), true)
        builder.setDeleteIntent(getDeleteIntent(notificationId))
        notificationView = RemoteViews(context.packageName, R.layout.layout_small_call_notification)
        smallNotificationView = RemoteViews(context.packageName, R.layout.layout_small_call_notification_2)
        createNotificationView(notificationView!!, data = data)
        createNotificationView(smallNotificationView!!, data = data)
        builder.setCustomContentView(smallNotificationView)
        builder.setCustomBigContentView(notificationView)
        builder.setCustomHeadsUpContentView(smallNotificationView)
        val notification = builder.build()
        notification.flags = Notification.FLAG_INSISTENT
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationView(remoteViews: RemoteViews, data: Bundle){
        remoteViews.setTextViewText(
            R.id.tv_caller_name, data.getString(EXTRA_CALLER_NAME) ?: "Bank MAS"
        )
        remoteViews.setOnClickPendingIntent(
            R.id.iv_accept, getAcceptIntent(notificationId = data.getInt(EXTRA_NOTIFICATION_ID))
        )
        remoteViews.setOnClickPendingIntent(
            R.id.iv_decline, getDeclinedIntent(notificationId = data.getInt(EXTRA_NOTIFICATION_ID))
        )
    }

    private fun getFullScreenIntent(notificationId: Int):PendingIntent{
        println("masuk notifId $notificationId")
        val intent = Intent(context, FullScreenNotification::class.java)
        val data = Bundle()
        data.apply {
            putInt(EXTRA_NOTIFICATION_ID, notificationId)
        }
        intent.apply {
            putExtra(NotificationBroadcastReceiver.EXTRA_DATA, data)
        }
        return PendingIntent.getActivity(context, notificationId, intent, getFlagPendingIntent())
    }

    private fun getDeleteIntent(notificationId: Int):PendingIntent{
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val data = Bundle()
        data.apply {
            putInt(EXTRA_NOTIFICATION_ID, notificationId)
        }
        intent.apply {
            action = NotificationBroadcastReceiver.ACTION_DECLINED_CALL
            putExtra(NotificationBroadcastReceiver.EXTRA_DATA, data)
        }
        return PendingIntent.getBroadcast(context, notificationId, intent, getFlagPendingIntent())
    }

    private fun getAcceptIntent(notificationId: Int): PendingIntent {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val data = Bundle()
        data.apply {
            putInt(EXTRA_NOTIFICATION_ID, notificationId)
        }
        intent.apply {
            action = NotificationBroadcastReceiver.ACTION_ACCEPT_CALL
            putExtra(NotificationBroadcastReceiver.EXTRA_DATA, data)
        }
        return PendingIntent.getBroadcast(context, notificationId, intent, getFlagPendingIntent())
    }

    private fun getDeclinedIntent(notificationId:Int): PendingIntent {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val data = Bundle()
        data.apply {
            putInt(EXTRA_NOTIFICATION_ID, notificationId)
        }
        intent.apply {
            action = NotificationBroadcastReceiver.ACTION_DECLINED_CALL
            putExtra(NotificationBroadcastReceiver.EXTRA_DATA, data)
        }
        return PendingIntent.getBroadcast(context, notificationId, intent, getFlagPendingIntent())
    }

    fun cancelNotification(notificationId: Int){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    private fun getFlagPendingIntent():Int{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
    }
}