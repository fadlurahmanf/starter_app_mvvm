package com.fadlurahmanf.starterappmvvm.unknown.utils.fcm

import com.fadlurahmanf.starterappmvvm.unknown.utils.call.CallBroadcastReceiver
import com.fadlurahmanf.starterappmvvm.unknown.utils.logging.logd
import com.fadlurahmanf.starterappmvvm.unknown.utils.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notificationHelper = NotificationHelper(applicationContext)
        logd("onMessageReceived title: ${message.notification?.title}")
        logd("onMessageReceived body: ${message.notification?.body}")
        logd("onMessageReceived data: ${message.data}")
        if (message.data["type"] == "incoming-videocall"){
            CallBroadcastReceiver.sendBroadcastIncomingCall(applicationContext)
        }else if(message.data["type"] == "stop-videocall"){
            CallBroadcastReceiver.sendBroadcastDeclinedCall(applicationContext)
        }else{
            if(message.notification?.title != null && message.notification?.body != null){
                notificationHelper.showNotification(1, message.notification!!.title!!, message.notification!!.body!!)
            }else if (message.data["title"] != null && message.data["body"] != null){
                notificationHelper.showNotification(1, message.data["title"]!!, message.data["body"]!!)
            }
        }
    }
}