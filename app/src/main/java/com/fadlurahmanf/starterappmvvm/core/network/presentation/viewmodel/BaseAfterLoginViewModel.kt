package com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BaseAfterLoginViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val authSpStorage: AuthSpStorage
) : BaseViewModel() {
    private var _refreshTokenState = MutableLiveData<CustomState<BaseResponse<LoginResponse>>>()
    val refreshTokenState get():LiveData<CustomState<BaseResponse<LoginResponse>>> = _refreshTokenState

    fun refreshToken() {
        val request = RefreshTokenRequest(authSpStorage.refreshToken ?: "")
        disposable().add(authenticationInteractor.refreshToken(request).observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { baseResponse ->
                    baseResponse.data?.let { loginResponse ->
                        authenticationInteractor.saveAuthToken(loginResponse)
                    }
                },
                {
                    println("MASUK ERROR ${it.message}")
                },
                {}
            ))
    }
}