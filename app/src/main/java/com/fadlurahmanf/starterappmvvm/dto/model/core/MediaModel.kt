package com.fadlurahmanf.starterappmvvm.dto.model.core

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

enum class ImageOrigin{
    URI, NETWORK, ASSET
}

@Parcelize
data class ImageModel(
    var origin: ImageOrigin,
    var path:String
) : Parcelable

@Parcelize
data class AlbumGalleryModel(
    var bucketName:String ?= null, /** Album Name */
    var bucketId:Long ?= null, /** Album Id */
    var medias:List<MediaGalleryModel> = listOf()
) : Parcelable

@Parcelize
data class MediaGalleryModel(
    var id:Long ?= null,
    var path:String ?= null,
    var bucketName:String ?= null, /** Album Name */
    var bucketId:Long ?= null, /** Album Id */
    var dateAdded:String ?= null
) : Parcelable
