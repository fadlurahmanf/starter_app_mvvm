package com.fadlurahmanf.starterappmvvm.core.unknown.domain.service

import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationImpl
import com.fadlurahmanf.starterappmvvm.unknown.utils.call.CallBroadcastReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private var notificationImpl: NotificationImpl? = null

    private fun getNotificationImpl(): NotificationImpl {
        if (notificationImpl == null) {
            notificationImpl = NotificationImpl(applicationContext)
        }
        return notificationImpl!!
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        logConsole.d("FCM TITLE: ${message.notification?.title}")
        logConsole.d("FCM BODY: ${message.notification?.body}")
        logConsole.d("FCM DATA: ${message.data}")
        if (message.data["type"] == "incoming-videocall") {
            CallBroadcastReceiver.sendBroadcastIncomingCall(applicationContext)
        } else if (message.data["type"] == "stop-videocall") {
            CallBroadcastReceiver.sendBroadcastDeclinedCall(applicationContext)
        } else {
            val messageTitle = message.data["title"]
            val messageBody = message.data["body"]
            if (messageTitle != null && messageBody != null) {
                getNotificationImpl().showNotification(
                    title = messageTitle,
                    body = messageBody
                )
            } else if (message.notification?.title != null && message.notification?.body != null) {
                getNotificationImpl().showNotification(
                    title = message.notification?.title ?: "",
                    body = message.notification?.body ?: ""
                )
            }
        }
    }
}