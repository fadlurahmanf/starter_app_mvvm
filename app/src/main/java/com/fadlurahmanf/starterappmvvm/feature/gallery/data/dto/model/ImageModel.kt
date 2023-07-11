package com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model

import android.os.Parcelable
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.constant.ImageOrigin
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    var origin: ImageOrigin,
    var path:String
) : Parcelable