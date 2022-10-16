package com.fadlurahmanf.starterappmvvm.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class PdfOrigin{
    NETWORK, FILE, ASSET
}

@Parcelize
data class PdfModel(
    var origin: PdfOrigin,
    var path:String
) : Parcelable
