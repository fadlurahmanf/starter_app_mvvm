package com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.response.CreateGuestTokenResponse
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toErrorState
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.UUID
import javax.inject.Inject

class ExampleSwitchDomainViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val authSpStorage: AuthSpStorage
) : BaseViewModel() {

    private val _guestTokenState = MutableLiveData<CustomState<CreateGuestTokenResponse>>()
    val guestToken get():LiveData<CustomState<CreateGuestTokenResponse>> = _guestTokenState

    fun getGuestToken() {
        _guestTokenState.value = CustomState.Idle
        _guestTokenState.value = CustomState.Loading
        val request = CreateGuestTokenRequest(
            guestId = UUID.randomUUID().toString()
        )
        compositeDisposable().add(authenticationInteractor.createGuestToken(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { baseResponse ->
                    if (isSuccess(baseResponse)) {
                        val data = baseResponse.data
                        if (data?.accessToken != null) {
                            authenticationInteractor.saveGuestToken(data.accessToken ?: "")
                            _guestTokenState.value = CustomState.Success(data = data)
                        } else {
                            _guestTokenState.value = baseResponse.message?.toErrorState()
                        }
                    } else {
                        _guestTokenState.value = baseResponse.message?.toErrorState()
                    }
                },
                {
                    _guestTokenState.value = it.toErrorState()
                },
                {}
            ))
    }
}