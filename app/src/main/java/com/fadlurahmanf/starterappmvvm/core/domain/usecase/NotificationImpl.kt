package com.fadlurahmanf.starterappmvvm.core.domain.usecase

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseNotification
import com.fadlurahmanf.starterappmvvm.core.external.constant.AppKey

class NotificationImpl(context: Context) : BaseNotification(context) {
    override val channelId: String
        get() = AppKey.Notification.GENERAL_CHANNEL_ID
    override val channel: String
        get() = AppKey.Notification.GENERAL_CHANNEL
    override val description: String
        get() = AppKey.Notification.GENERAL_DESCRIPTION
}