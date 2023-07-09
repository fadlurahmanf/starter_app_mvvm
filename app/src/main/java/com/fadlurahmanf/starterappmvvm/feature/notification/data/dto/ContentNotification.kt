package com.fadlurahmanf.starterappmvvm.feature.notification.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ContentNotification(
    val type: String,
    val data: @RawValue Map<String, Any>?= null
) : Parcelable