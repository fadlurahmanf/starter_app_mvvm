package com.fadlurahmanf.starterappmvvm.unknown.data.repository.example

import com.fadlurahmanf.starterappmvvm.unknown.data.api.datasource.example.IdentityDatasource
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import com.google.gson.JsonObject
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