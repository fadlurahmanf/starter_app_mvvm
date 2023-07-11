package com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryItemModel(
    var id: Long? = null,
    var path: String,
    /** Album Name */
    var albumName: String? = null,
    /** Album Id */
    var albumId: Long? = null,
    var dateAdded: String? = null
) : Parcelable