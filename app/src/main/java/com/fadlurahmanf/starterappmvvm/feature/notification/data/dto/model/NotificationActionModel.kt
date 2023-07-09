package com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model

import android.app.PendingIntent

data class NotificationActionModel(
    var icon: Int,
    var title: String,
    var pendingIntent: PendingIntent? = null
)
