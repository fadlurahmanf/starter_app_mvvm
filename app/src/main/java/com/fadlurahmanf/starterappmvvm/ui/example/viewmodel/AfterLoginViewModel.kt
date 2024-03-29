package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.base.BaseViewModel
import com.fadlurahmanf.starterappmvvm.base.NetworkState
import com.fadlurahmanf.starterappmvvm.data.repository.example.CIFRepository
import com.fadlurahmanf.starterappmvvm.dto.response.example.FavoriteResponse
import com.fadlurahmanf.starterappmvvm.extension.toErrorState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AfterLoginViewModel @Inject constructor(
    private var cifRepository: CIFRepository
) : BaseViewModel() {
    private var _favoritesState = MutableLiveData<NetworkState<List<FavoriteResponse>>>()
    val favoriteState get() = _favoritesState

    fun getFavorite(){
        _favoritesState.value = NetworkState.Idle
        _favoritesState.value = NetworkState.Loading
        disposable().add(cifRepository.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (isVerify(it)){
                        _favoritesState.value = NetworkState.Success(
                            data = it.data!!
                        )
                    }else{
                        _favoritesState.value = (it.message?:"").toErrorState()
                    }
                },
                {
                    _favoritesState.value = it.toErrorState()
                },
                {}
            ))
    }
}