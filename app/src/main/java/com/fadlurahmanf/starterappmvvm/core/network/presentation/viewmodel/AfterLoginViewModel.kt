package com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.FavoriteInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseAfterLoginViewModel
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toErrorState
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AfterLoginViewModel @Inject constructor(
    private val favoriteInteractor: FavoriteInteractor,
    private val authenticationInteractor: AuthenticationInteractor,
    private val authSpStorage: AuthSpStorage,
) : BaseAfterLoginViewModel(authenticationInteractor, authSpStorage) {
    private var _favoritesState = MutableLiveData<CustomState<List<FavoriteResponse>>>()
    val favoriteState get() = _favoritesState

    fun getFavorite(debugForceRefreshToken:Boolean) {
        _favoritesState.value = CustomState.Idle
        _favoritesState.value = CustomState.Loading
        compositeDisposable().add(favoriteInteractor.getFavorites(debugForceRefreshToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (isSuccess(it)) {
                        _favoritesState.value = CustomState.Success(
                            data = it.data!!
                        )
                    } else {
                        _favoritesState.value = (it.message ?: "").toErrorState()
                    }
                },
                {
                    _favoritesState.value = it.toErrorState()
                },
                {}
            ))
    }
}