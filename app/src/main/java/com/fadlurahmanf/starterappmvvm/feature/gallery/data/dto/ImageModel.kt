package com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    var origin: ImageOrigin,
    var path:String
) : Parcelable