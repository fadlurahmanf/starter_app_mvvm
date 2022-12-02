package com.fadlurahmanf.starterappmvvm.dto.model.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class ImageOrigin{
    URI, NETWORK, ASSET
}

@Parcelize
data class ImageModel(
    var origin: ImageOrigin,
    var path:String
) : Parcelable
