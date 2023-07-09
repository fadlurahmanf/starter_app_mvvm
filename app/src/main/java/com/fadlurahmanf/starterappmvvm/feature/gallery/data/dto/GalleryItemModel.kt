package com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryItemModel(
    var id: Long? = null,
    var path: String,
    var bucketName: String? = null,
    /** Album Name */
    var bucketId: Long? = null,
    /** Album Id */
    var dateAdded: String? = null
) : Parcelable