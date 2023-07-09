package com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases

import android.content.Context
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.common.BaseNotification
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.feature.notification.data.constant.NotificationConstant

class NotificationImpl(context: Context) : BaseNotification(context) {
    override val channelId: String
        get() = NotificationConstant.GENERAL_CHANNEL_ID
    override val channel: String
        get() = NotificationConstant.GENERAL_CHANNEL
    override val description: String
        get() = NotificationConstant.GENERAL_DESCRIPTION
}