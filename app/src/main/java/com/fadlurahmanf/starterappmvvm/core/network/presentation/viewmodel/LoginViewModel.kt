package com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.LoginRequest
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.response.LoginResponse
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toErrorState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor
) : BaseViewModel() {
    private var _loginState = MutableLiveData<CustomState<LoginResponse>>()
    val loginState get() = _loginState

    fun login() {
        _loginState.value = CustomState.Idle
        _loginState.value = CustomState.Loading
        val request = LoginRequest(
            nik = "3511101010101010",
            deviceId = "154adc5da72877dcfcc1eb8447434a6c67c9a402d68015aa8c31a074b0a7e82e",
            deviceToken = "fKhNx61jcUDnukmPFT-gB4:APA91bFBlL5a_LLWlda1JEU36_AYEOyTPM3poy-pz8yylxRlVc96FvnaPetkEOgyLKgRdhOgitz0Tztye0IV2J3n0RaGeidkC7lhbQRo2ZkEzvT6FHupwLiVJcFhD22Jlywvf0bJsMc0",
            password = "dfjZVW2OVvA/ujatBEs3QA==",
            phoneNumber = "081283602320",
            activationId = "69ed4697-5258-48ab-bfa6-e3f19264f417",
            clientTimeMilis = System.currentTimeMillis().toString()
        )
        compositeDisposable().add(authenticationInteractor.login(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { baseResponse ->
                    if (isSuccessWithoutMessage(baseResponse)) {
                        baseResponse.data?.let { loginResponse ->
                            saveToken(loginResponse)
                        }
                        _loginState.value = CustomState.Success(data = baseResponse.data!!)
                    } else {
                        _loginState.value = (baseResponse.message ?: "").toErrorState()
                    }
                },
                {
                    _loginState.value = it.toErrorState()
                },
                {}
            ))
    }

    private fun saveToken(data: LoginResponse) {
        authenticationInteractor.saveAuthToken(data)
    }
}