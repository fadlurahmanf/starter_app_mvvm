package com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GalleryAlbumModel(
    /** Album Name */
    var bucketName: String,
    /** Album Id */
    var bucketId: Long,
    var medias: List<GalleryItemModel> = listOf()
) : Parcelable