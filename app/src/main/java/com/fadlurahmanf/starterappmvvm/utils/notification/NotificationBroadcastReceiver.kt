package com.fadlurahmanf.starterappmvvm.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationBroadcastReceiver: BroadcastReceiver(){
    private lateinit var notifHelper:CallNotificationCallHelper
    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"

        const val ACTION_CALL_INCOMING = "com.fadlurahmanf.callkit.ACTION_CALL_INCOMING"
        const val ACTION_ACCEPT_CALL = "com.fadlurahmanf.callkit.ACTION_ACCEPT_CALL"
        const val ACTION_DECLINED_CALL = "com.fadlurahmanf.callkit.ACTION_DECLINED_CALL"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        notifHelper = CallNotificationCallHelper(context = context)
        val action = intent?.action
        Log.d("NotifBroadcaster", "onReceive: $action")
        when(action){
            ACTION_CALL_INCOMING -> {
                if (intent.extras != null){
                    notifHelper.showIncomingCallNotification(intent.extras!!.getBundle(EXTRA_DATA)!!)
                    val mIntentBroadcast = Intent(context, NotificationPlayerService::class.java)
                    context.startService(mIntentBroadcast)
                }
            }
            ACTION_ACCEPT_CALL -> {
                notifHelper.cancelNotification(notificationId = intent.getIntExtra(CallNotificationCallHelper.EXTRA_NOTIFICATION_ID, 0))
                context.stopService(Intent(context, NotificationPlayerService::class.java))
            }
            ACTION_DECLINED_CALL -> {
                notifHelper.cancelNotification(notificationId = intent.getIntExtra(CallNotificationCallHelper.EXTRA_NOTIFICATION_ID, 0))
                context.stopService(Intent(context, NotificationPlayerService::class.java))
            }
        }
    }
}