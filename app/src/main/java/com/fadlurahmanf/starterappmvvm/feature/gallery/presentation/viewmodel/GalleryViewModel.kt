package com.fadlurahmanf.starterappmvvm.feature.gallery.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.external.extension.toErrorState
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.GalleryAlbumModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.GalleryItemModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.domain.usecases.FetchGalleryUseCase
import javax.inject.Inject

class GalleryViewModel @Inject constructor(
    private val fetchGalleryUseCase: FetchGalleryUseCase
) : BaseViewModel() {
    private var _getAlbumState = MutableLiveData<CustomState<ArrayList<GalleryAlbumModel>>>()
    val albumState get() = _getAlbumState
    fun getAlbum(allowedExtension: List<String>?) {
        try {
            _getAlbumState.value = CustomState.Idle
            _getAlbumState.value = CustomState.Loading
            _getAlbumState.value = CustomState.Success(data = fetchGalleryUseCase.getAll())
        } catch (e: Throwable) {
            _getAlbumState.value = e.toErrorState()
        }
    }

    private var _getItemState = MutableLiveData<CustomState<ArrayList<GalleryItemModel>>>()
    val itemState get() = _getItemState
    fun getPhotos(allowedExtension: List<String>?) {
        try {
            _getItemState.value = CustomState.Idle
            _getItemState.value = CustomState.Loading
            val data = fetchGalleryUseCase.getPhotos()
            _getItemState.value = CustomState.Success(data = data)
        } catch (e: Throwable) {
            _getItemState.value = e.toErrorState()
        }
    }
}