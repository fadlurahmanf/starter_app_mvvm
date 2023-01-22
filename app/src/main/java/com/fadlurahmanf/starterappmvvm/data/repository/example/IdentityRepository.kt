package com.fadlurahmanf.starterappmvvm.data.repository.example

import com.fadlurahmanf.starterappmvvm.data.api.datasource.example.IdentityDatasource
import com.fadlurahmanf.starterappmvvm.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IdentityRepository @Inject constructor(
    private var identityDataSource: IdentityDatasource,
    private var authSpStorage: AuthSpStorage
){
    fun login(body:JsonObject) = identityDataSource.login(body)

    fun saveAuthToken(
        loginResponse: LoginResponse
    ){
        if (loginResponse.accessToken != null){
            authSpStorage.accessToken = loginResponse.accessToken
        }
        if (loginResponse.refreshToken != null){
            authSpStorage.refreshToken = loginResponse.refreshToken
        }
    }

    fun syncRefreshToken(body: JsonObject) = identityDataSource.syncRefreshToken(body)
}