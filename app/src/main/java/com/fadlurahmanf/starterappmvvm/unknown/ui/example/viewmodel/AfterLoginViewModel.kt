package com.fadlurahmanf.starterappmvvm.unknown.ui.example.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.data.repository.example.CIFRepository
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import com.fadlurahmanf.starterappmvvm.core.external.extension.toErrorState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AfterLoginViewModel @Inject constructor(
    private var cifRepository: CIFRepository
) : BaseViewModel() {
    private var _favoritesState = MutableLiveData<CustomState<List<FavoriteResponse>>>()
    val favoriteState get() = _favoritesState

    fun getFavorite(){
        _favoritesState.value = CustomState.Idle
        _favoritesState.value = CustomState.Loading
        disposable().add(cifRepository.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (isVerify(it)){
                        _favoritesState.value = CustomState.Success(
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