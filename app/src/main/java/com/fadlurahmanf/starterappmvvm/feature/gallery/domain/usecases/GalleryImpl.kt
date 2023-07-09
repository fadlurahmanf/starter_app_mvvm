package com.fadlurahmanf.starterappmvvm.feature.gallery.domain.usecases

import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.GalleryAlbumModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.GalleryItemModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.domain.datasource.GalleryDatasource
import javax.inject.Inject

class GalleryImpl @Inject constructor(
    private val galleryDatasource: GalleryDatasource
) {
    /**
     * filtering extension, only extension inside list will return
     * ex: if allowed extension [".jpg"], only return list of jpg image, ignore case
     * */
    private fun isExtensionAllowed(
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

    fun getAll(
        allowedExtensions: List<String>? = null
    ): ArrayList<GalleryAlbumModel> {
        try {
            val videos = galleryDatasource.getVideos(allowedExtensions)
            val photos = galleryDatasource.getPhotos(allowedExtensions)
            val all = videos.apply {
                addAll(photos)
            }
            val mapAlbumId: HashMap<Long, ArrayList<GalleryItemModel>> = hashMapOf()
            val mapAlbumName: HashMap<Long, String> = hashMapOf()
            all.forEach { p0 ->
                if (p0.albumId != null && isExtensionAllowed(allowedExtensions, p0.path)) {
                    val albumId = p0.albumId!!
                    mapAlbumName[albumId] = p0.albumName ?: ""
                    if (mapAlbumId.containsKey(albumId)) {
                        val list: ArrayList<GalleryItemModel> = mapAlbumId[albumId]!!
                        list.add(p0)
                        mapAlbumId[albumId] = list
                    } else {
                        val list = arrayListOf<GalleryItemModel>().apply {
                            add(p0)
                        }
                        mapAlbumId[albumId] = list
                    }
                }
            }
            return ArrayList(mapAlbumId.map {
                GalleryAlbumModel(
                    bucketId = it.key,
                    bucketName = mapAlbumName[it.key] ?: "Umum/Lainnya",
                    medias = it.value
                )
            }.toList())
        } catch (e: Throwable) {
            logConsole.e("GET ALL VIDEOS & IMAGE: ${e.message}")
            throw e
        }
    }

    /**
     * Get all videos in album
     * */
    fun getAlbumVideos(
        allowedExtensions: List<String>? = null
    ): ArrayList<GalleryAlbumModel> {
        try {
            val videos = galleryDatasource.getVideos(allowedExtensions)
            val mapAlbumId: HashMap<Long, ArrayList<GalleryItemModel>> = hashMapOf()
            val mapAlbumName: HashMap<Long, String> = hashMapOf()
            videos.forEach { p0 ->
                if (p0.albumId != null && isExtensionAllowed(allowedExtensions, p0.path)) {
                    val albumId = p0.albumId!!
                    mapAlbumName[albumId] = p0.albumName ?: ""
                    if (mapAlbumId.containsKey(albumId)) {
                        val list: ArrayList<GalleryItemModel> = mapAlbumId[albumId]!!
                        list.add(p0)
                        mapAlbumId[albumId] = list
                    } else {
                        val list = arrayListOf<GalleryItemModel>().apply {
                            add(p0)
                        }
                        mapAlbumId[albumId] = list
                    }
                }
            }
            return ArrayList(mapAlbumId.map {
                GalleryAlbumModel(
                    bucketId = it.key,
                    bucketName = mapAlbumName[it.key] ?: "Umum/Lainnya",
                    medias = it.value
                )
            }.toList())
        } catch (e: Throwable) {
            logConsole.e("GET ALL PHOTOS IN ALBUM: ${e.message}")
            throw e
        }
    }

    /**
     * Get all videos
     * */
    fun getVideos(
        allowedExtensions: List<String>? = null
    ): ArrayList<GalleryItemModel> {
        try {
            val videos = galleryDatasource.getVideos(allowedExtensions)
            val list = arrayListOf<GalleryItemModel>()
            videos.forEach { p0 ->
                if (isExtensionAllowed(allowedExtensions, p0.path)) {
                    list.add(p0)
                }
            }
            return list
        } catch (e: Throwable) {
            logConsole.e("GET ALL PHOTOS IN ALBUM: ${e.message}")
            throw e
        }
    }

    /**
     * Get all photos in album
     * */
    fun getAlbumPhotos(
        allowedExtensions: List<String>? = null
    ): ArrayList<GalleryAlbumModel> {
        try {
            val photos = galleryDatasource.getPhotos(allowedExtensions)
            val mapAlbumId: HashMap<Long, ArrayList<GalleryItemModel>> = hashMapOf()
            val mapAlbumName: HashMap<Long, String> = hashMapOf()
            photos.forEach { p0 ->
                if (p0.albumId != null && isExtensionAllowed(allowedExtensions, p0.path)) {
                    val albumId = p0.albumId!!
                    mapAlbumName[albumId] = p0.albumName ?: ""
                    if (mapAlbumId.containsKey(albumId)) {
                        val list: ArrayList<GalleryItemModel> = mapAlbumId[albumId]!!
                        list.add(p0)
                        mapAlbumId[albumId] = list
                    } else {
                        val list = arrayListOf<GalleryItemModel>().apply {
                            add(p0)
                        }
                        mapAlbumId[albumId] = list
                    }
                }
            }
            return ArrayList(mapAlbumId.map {
                GalleryAlbumModel(
                    bucketId = it.key,
                    bucketName = mapAlbumName[it.key] ?: "Umum/Lainnya",
                    medias = it.value
                )
            }.toList())
        } catch (e: Throwable) {
            logConsole.e("GET ALL PHOTOS IN ALBUM: ${e.message}")
            throw e
        }
    }

    /**
     * Get all photos
     * */
    fun getPhotos(
        allowedExtensions: List<String>? = null
    ): ArrayList<GalleryItemModel> {
        try {
            val photos = galleryDatasource.getPhotos(allowedExtensions)
            val list = arrayListOf<GalleryItemModel>()
            photos.forEach { p0 ->
                if (isExtensionAllowed(allowedExtensions, p0.path)) {
                    list.add(p0)
                }
            }
            return list
        } catch (e: Throwable) {
            logConsole.e("GET ALL PHOTOS IN ALBUM: ${e.message}")
            throw e
        }
    }
}