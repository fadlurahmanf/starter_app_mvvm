package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.event.RxEvent
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.external.helper.RxBus
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
    private var _refreshTokenState = MutableLiveData<CustomState<BaseResponse<LoginResponse>>>()
    val refreshTokenState get():LiveData<CustomState<BaseResponse<LoginResponse>>> = _refreshTokenState

    init {
        listenActionRefreshToken()
    }

    fun refreshToken() {
        val request = RefreshTokenRequest(authSpStorage.refreshToken ?: "")
        compositeDisposable().add(authenticationInteractor.refreshToken(request)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { baseResponse ->
                    baseResponse.data?.let { loginResponse ->
                        authenticationInteractor.saveAuthToken(loginResponse)
                        RxBus.publish(RxEvent.ResetTimerAfterLogin())
                    }
                },
                {
                    println("MASUK ERROR ${it.message}")
                },
                {}
            ))
    }

    private fun listenActionRefreshToken() {
        logConsole.d("listenActionRefreshToken")
        compositeDisposable().add(RxBus.listen(RxEvent.RefreshToken::class.java).subscribe {
            logConsole.d("EVENT RefreshToken")
            refreshToken()
            compositeDisposable().clear()
        })
    }
}

abstract class BaseViewModel : ViewModel() {
    fun compositeDisposable() = CompositeDisposable()

    fun <T> isSuccess(response: BaseResponse<T>) = response.code == "200"
            && response.message == "SUCCESS"
            && response.data != null

    fun <T> isSuccessWithoutMessage(response: BaseResponse<T>) = response.code == "200"
            && response.data != null
}