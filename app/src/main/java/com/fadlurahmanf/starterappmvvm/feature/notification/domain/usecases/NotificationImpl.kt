package com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases

import android.content.Context
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.common.BaseNotification
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey

class NotificationImpl(context: Context) : BaseNotification(context) {
    override val channelId: String
        get() = AppKey.Notification.GENERAL_CHANNEL_ID
    override val channel: String
        get() = AppKey.Notification.GENERAL_CHANNEL
    override val description: String
        get() = AppKey.Notification.GENERAL_DESCRIPTION
}