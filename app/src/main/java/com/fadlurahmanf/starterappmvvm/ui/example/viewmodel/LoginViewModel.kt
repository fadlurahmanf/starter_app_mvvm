package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.base.BaseViewModel
import com.fadlurahmanf.starterappmvvm.base.NetworkState
import com.fadlurahmanf.starterappmvvm.data.repository.example.IdentityRepository
import com.fadlurahmanf.starterappmvvm.dto.response.example.LoginResponse
import com.fadlurahmanf.starterappmvvm.extension.toErrorState
import com.google.gson.JsonObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    var identityRepository: IdentityRepository
):BaseViewModel() {
    private var _loginState = MutableLiveData<NetworkState<LoginResponse>>()
    val loginState get() = _loginState

    fun login(){
        _loginState.value = NetworkState.Idle
        _loginState .value = NetworkState.Loading
        val body = JsonObject()
        body.apply {
            addProperty("deviceId", "2562DA2A-57E7-47D9-94E1-05E11EA96F6D-1668481305454")
            addProperty("deviceToken", "1")
            addProperty("phoneNumber", "08123123123")
            addProperty("password", "Abcd1234")
        }
        disposable().add(identityRepository.login(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (isCodeVerify(it)){
                        _loginState.value = NetworkState.Success(data = it.data!!)
                    }else{
                        _loginState.value = (it.message?:"").toErrorState()
                    }
                },
                {
                    _loginState.value = it.toErrorState()
                },
                {}
            ))
    }

    fun saveToken(data:LoginResponse){
        identityRepository.saveAuthToken(data)
    }
}