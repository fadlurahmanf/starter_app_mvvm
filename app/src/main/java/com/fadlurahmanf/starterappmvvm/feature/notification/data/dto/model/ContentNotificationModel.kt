package com.fadlurahmanf.starterappmvvm.feature.notification.data.dto.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ContentNotificationModel(
    val type: String,
    val data: @RawValue Map<String, Any>?= null
) : Parcelable