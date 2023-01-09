package com.fadlurahmanf.starterappmvvm.utils.call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.starterappmvvm.constant.NotificationConstant
import com.fadlurahmanf.starterappmvvm.ui.example.activity.CallActivity

class CallBroadcastReceiver: BroadcastReceiver(){
    private lateinit var notifHelper:CallNotificationHelper
    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
        const val ACTION_CALL_INCOMING = "com.fadlurahmanf.callkit.ACTION_CALL_INCOMING"
        const val ACTION_ACCEPT_CALL = "com.fadlurahmanf.callkit.ACTION_ACCEPT_CALL"
        const val ACTION_DECLINED_CALL = "com.fadlurahmanf.callkit.ACTION_DECLINED_CALL"

        fun sendBroadcast(context: Context, action:String, data:Bundle){
            val intent = Intent(context, CallBroadcastReceiver::class.java)
            intent.apply {
                this.action = action
                putExtra(EXTRA_DATA, data)
            }
            context.sendBroadcast(intent)
        }

        fun sendBroadcastIncomingCall(context: Context, actionCallerName:String = "DEFAULT ACTION CALLER NAME"){
            val intent = Intent(context, CallBroadcastReceiver::class.java)
            val data = Bundle()
            data.apply {
                putInt(CallNotificationHelper.EXTRA_NOTIFICATION_ID, NotificationConstant.INCOMING_CALL_NOTIFICATION_ID)
                putString(CallNotificationHelper.EXTRA_CALLER_NAME, actionCallerName)
            }
            intent.apply {
                this.action = ACTION_CALL_INCOMING
                putExtra(EXTRA_DATA, data)
            }
            context.sendBroadcast(intent)
        }

        fun sendBroadcastAcceptCall(context: Context){
            val intent = Intent(context, CallBroadcastReceiver::class.java)
            val data = Bundle()
            data.apply {
                putInt(CallNotificationHelper.EXTRA_NOTIFICATION_ID, NotificationConstant.INCOMING_CALL_NOTIFICATION_ID)
            }
            intent.apply {
                this.action = ACTION_ACCEPT_CALL
                putExtra(EXTRA_DATA, data)
            }
            context.sendBroadcast(intent)
        }

        fun sendBroadcastDeclinedCall(context: Context){
            val intent = Intent(context, CallBroadcastReceiver::class.java)
            val data = Bundle()
            data.apply {
                putInt(CallNotificationHelper.EXTRA_NOTIFICATION_ID, NotificationConstant.INCOMING_CALL_NOTIFICATION_ID)
            }
            intent.apply {
                this.action = ACTION_DECLINED_CALL
                putExtra(EXTRA_DATA, data)
            }
            context.sendBroadcast(intent)
        }

        fun getIntentIncomingCall(context: Context, actionCallerName:String = "DEFAULT ACTION CALLER NAME"):Intent{
            val intent = Intent(context, CallBroadcastReceiver::class.java)
            val data = Bundle()
            data.apply {
                putInt(CallNotificationHelper.EXTRA_NOTIFICATION_ID, NotificationConstant.INCOMING_CALL_NOTIFICATION_ID)
                putString(CallNotificationHelper.EXTRA_CALLER_NAME, actionCallerName)
            }
            intent.apply {
                this.action = ACTION_CALL_INCOMING
                putExtra(EXTRA_DATA, data)
            }
            return intent
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("NotifBroadcaster", "onReceive: action -> ${intent?.action}")
        if (context == null) return
        notifHelper = CallNotificationHelper(context = context)
        when(intent?.action){
            ACTION_CALL_INCOMING -> {
                if (intent.extras != null){
                    notifHelper.showIncomingCallNotification(intent.extras!!.getBundle(EXTRA_DATA)!!)
                    val mIntentBroadcast = Intent(context, CallNotificationPlayerService::class.java)
                    context.startService(mIntentBroadcast)
                }
            }
            ACTION_ACCEPT_CALL -> {
                val notificationId:Int = intent.extras?.getBundle(EXTRA_DATA)?.getInt(CallNotificationHelper.EXTRA_NOTIFICATION_ID)!!
                notifHelper.endedCallNotification(notificationId = notificationId)
                context.stopService(Intent(context, CallNotificationPlayerService::class.java))
                val p0Intent = Intent(context, CallActivity::class.java)
                p0Intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(p0Intent)
            }
            ACTION_DECLINED_CALL -> {
                val notificationId:Int = intent.extras?.getBundle(EXTRA_DATA)?.getInt(CallNotificationHelper.EXTRA_NOTIFICATION_ID)!!
                notifHelper.endedCallNotification(notificationId = notificationId)
                context.stopService(Intent(context, CallNotificationPlayerService::class.java))
            }
        }
    }
}