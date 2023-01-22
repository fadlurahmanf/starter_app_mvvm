package com.fadlurahmanf.starterappmvvm.utils.fcm

import com.fadlurahmanf.starterappmvvm.utils.logging.logd
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMHelper:FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logd("onMessageReceived: ${message.data}")
    }
}