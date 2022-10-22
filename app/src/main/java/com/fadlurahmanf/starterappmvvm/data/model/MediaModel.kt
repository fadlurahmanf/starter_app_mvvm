package com.fadlurahmanf.starterappmvvm.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class ImageOrigin{
    URI, NETWORK, ASSET
}

@Parcelize
data class ImageModel(
    var origin:ImageOrigin,
    var path:String
) : Parcelable
