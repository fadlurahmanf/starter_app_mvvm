package com.fadlurahmanf.starterappmvvm.utils.fcm

import com.fadlurahmanf.starterappmvvm.utils.call.CallBroadcastReceiver
import com.fadlurahmanf.starterappmvvm.utils.call.CallNotificationHelper
import com.fadlurahmanf.starterappmvvm.utils.logging.logd
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMHelper:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logd("onMessageReceived title: ${message.notification?.title}")
        logd("onMessageReceived body: ${message.notification?.body}")
        logd("onMessageReceived data: ${message.data}")
        if (message.data["type"] == "incoming-videocall"){
            CallBroadcastReceiver.sendBroadcastIncomingCall(applicationContext)
        }else if(message.data["type"] == "stop-videocall"){
            CallBroadcastReceiver.sendBroadcastDeclinedCall(applicationContext)
        }
    }
}