package com.fadlurahmanf.starterappmvvm.feature.gallery.domain.usecases

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.GalleryAlbumModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.GalleryItemModel
import java.lang.Exception

@Deprecated("DEPRECATED ")
class GalleryHelper(var context: Context) {
    companion object {
        private fun filterExtension(
            allowedExtensions: List<String>? = null,
            path: String
        ): Boolean {
            if (allowedExtensions == null) return true
            var result = false
            for (i in allowedExtensions.indices) {
                if (path.lowercase()
                        .contains(allowedExtensions[i].lowercase(), ignoreCase = true)
                ) {
                    result = true
                    break
                }
            }
            return result
        }

        /**
         * only allowedExtensions can return. ex: [".mp4", ".jpg"]
         * if allowedExtensions is null means you want return all of images and videos
         **/
        fun getAll(
            context: Context,
            allowedExtensions: List<String>? = null
        ): ArrayList<GalleryAlbumModel> {
            try {
                val videos = getVideos(context, allowedExtensions)
                val photos = getPhotos(context, allowedExtensions)
                val all = videos.apply {
                    addAll(photos)
                }
                val mapBasedId: HashMap<Long, ArrayList<GalleryItemModel>> = hashMapOf()
                val mapBasedBucketName: HashMap<Long, String> = hashMapOf()
                all.forEach { p0 ->
                    p0.albumId?.let {
                        mapBasedBucketName[it] = p0.albumName ?: ""
                        if (mapBasedId.containsKey(it)) {
                            val list: ArrayList<GalleryItemModel> = mapBasedId[it]!!
                            list.add(p0)
                            mapBasedId[it] = list
                        } else {
                            val list = arrayListOf<GalleryItemModel>().apply {
                                add(p0)
                            }
                            mapBasedId[it] = list
                        }
                    }
                }
                return ArrayList(mapBasedId.map {
                    GalleryAlbumModel(
                        bucketId = it.key,
                        bucketName = mapBasedBucketName[it.key] ?: "Umum/Lainnya",
                        medias = it.value
                    )
                }.toList())
            } catch (e: Exception) {
                Log.e("CompanionGalleryHelper", "getAll: ${e.message}")
                return arrayListOf()
            }
        }

        /**
         * only allowedExtensions can return. ex: [".mp4"]
         * if allowedExtensions is null means you want return all of videos
         **/
        fun getVideos(
            context: Context,
            allowedExtensions: List<String>? = null
        ): ArrayList<GalleryItemModel> {
            try {
                val videoProjection = arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Media.BUCKET_ID,
                    MediaStore.Video.Media.DATE_ADDED
                )
                val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
                val cursor = context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    videoProjection,
                    null,
                    null,
                    sortOrder
                )

                val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val dataColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val bucketNameColumn =
                    cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                val bucketIdColumn = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
                val dateAddedColumn =
                    cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

                val list = arrayListOf<GalleryItemModel>()

                while (cursor?.moveToNext() == true) {
                    val id = idColumn?.let { cursor.getLong(it) }
                    val path = dataColumn?.let { cursor.getString(it) }
                    val bucket = bucketNameColumn?.let { cursor.getString(it) }
                    val bucketId = bucketIdColumn?.let { cursor.getLong(it) }
                    val date = dateAddedColumn?.let { cursor.getString(it) }
                    val contentUri = id?.let {
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            it
                        )
                    }
                    if (path != null && filterExtension(allowedExtensions, path)) {
                        list.add(
                            GalleryItemModel(
                                id = id,
                                path = path,
                                albumName = bucket,
                                albumId = bucketId,
                                dateAdded = date
                            )
                        )
                    }
                }
                cursor?.close()
                return list
            } catch (e: Exception) {
                Log.e("CompanionGalleryHelper", "getVideos: ${e.message}")
                return arrayListOf()
            }
        }

        /**
         * only allowedExtensions can return. ex: [".jpg", ".png"]
         * if allowedExtensions is null means you want return all of videos
         **/
        fun getPhotos(
            context: Context,
            allowedExtensions: List<String>? = null
        ): ArrayList<GalleryItemModel> {
            try {
                val imageProjection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.DATE_ADDED,
                )

                val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

                val cursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageProjection,
                    null,
                    null,
                    sortOrder
                )

                val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dataColumn = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val bucketNameColumn =
                    cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val buckedIdColumn =
                    cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val dateAddedColumn =
                    cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                val list = arrayListOf<GalleryItemModel>()

                while (cursor?.moveToNext() == true) {
                    val id = idColumn?.let { cursor.getLong(it) }
                    val path = dataColumn?.let { cursor.getString(it) }
                    val bucket = bucketNameColumn?.let { cursor.getString(it) }
                    val bucketId = buckedIdColumn?.let { cursor.getLong(it) }
                    val date = dateAddedColumn?.let { cursor.getString(it) }
                    val contentUri = id?.let {
                        ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            it
                        )
                    }

                    if (path != null && filterExtension(allowedExtensions, path)) {
                        list.add(
                            GalleryItemModel(
                                id = id,
                                path = path,
                                albumName = bucket,
                                albumId = bucketId,
                                dateAdded = date
                            )
                        )
                    }
                }
                cursor?.close()
                return list
            } catch (e: Exception) {
                Log.e("CompanionGalleryHelper", "getPhoto: ${e.message}")
                return arrayListOf()
            }
        }
    }

    /**
     * only allowedExtensions can return. ex: [".mp4", ".jpg"]
     * if allowedExtensions is null means you want return all of images and videos
     **/
    fun getAll(allowedExtensions: List<String>? = null): ArrayList<GalleryAlbumModel> {
        return try {
            getAll(context, allowedExtensions)
        } catch (e: Exception) {
            Log.e("GalleryHelper", "getAll: ${e.message}")
            arrayListOf()
        }
    }

    /**
     * only allowedExtensions can return. ex: [".mp4"]
     * if allowedExtensions is null means you want return all of videos
     **/
    fun getVideos(allowedExtensions: List<String>? = null): ArrayList<GalleryItemModel> {
        return try {
            getVideos(context, allowedExtensions)
        } catch (e: Exception) {
            Log.e("GalleryHelper", "getVideos: ${e.message}")
            arrayListOf()
        }
    }

    /**
     * only allowedExtensions can return. ex: [".jpg", ".png"]
     * if allowedExtensions is null means you want return all of videos
     **/
    fun getPhotos(allowedExtensions: List<String>? = null): ArrayList<GalleryItemModel> {
        return try {
            getPhotos(context, allowedExtensions)
        } catch (e: Exception) {
            Log.e("GalleryHelper", "getPhoto: ${e.message}")
            arrayListOf()
        }
    }
}