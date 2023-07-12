package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toErrorState
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class BaseAfterLoginViewModel(
    private val authenticationInteractor: AuthenticationInteractor,
    private val authSpStorage: AuthSpStorage
) : BaseViewModel() {
    private var _refreshTokenState = MutableLiveData<CustomState<LoginResponse>>()
    val refreshTokenState get():LiveData<CustomState<LoginResponse>> = _refreshTokenState

    fun refreshToken() {
        _refreshTokenState.value = CustomState.Idle
        _refreshTokenState.value = CustomState.Loading
        val request = RefreshTokenRequest(authSpStorage.refreshToken ?: "")
        disposable().add(authenticationInteractor.refreshToken(request).observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (isSuccess(response = it)) {
                        _refreshTokenState.value = CustomState.Success(data = it.data!!)
                    } else {
                        _refreshTokenState.value = it.message?.toErrorState()
                    }
                },
                {
                    _refreshTokenState.value = it.toErrorState()
                },
                {}
            ))
    }
}

abstract class BaseViewModel : ViewModel() {
    fun disposable() = CompositeDisposable()

    fun <T> isSuccess(response: BaseResponse<T>) = response.code == "200"
            && response.message == "SUCCESS"
            && response.data != null

    fun <T> isSuccessWithoutMessage(response: BaseResponse<T>) = response.code == "200"
            && response.data != null
}