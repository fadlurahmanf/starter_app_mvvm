package com.fadlurahmanf.starterappmvvm.feature.gallery.domain.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.GalleryItemModel
import javax.inject.Inject

class GalleryDatasource @Inject constructor(
    private val context: Context
) {
    fun getVideos(
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
                if (path != null) {
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
        } catch (e: Throwable) {
            logConsole.e("GET ALL VIDEOS: ${e.message}")
            throw e
        }
    }

    fun getPhotos(
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

                if (path != null) {
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
        } catch (e: Throwable) {
            logConsole.e("GET ALL PHOTOS: ${e.message}")
            throw e
        }
    }
}