package com.fadlurahmanf.starterappmvvm.core.network.domain.interactor

import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.LoginRequest
import com.fadlurahmanf.starterappmvvm.core.network.domain.repository.IdentityRemoteDatasource
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import com.google.gson.JsonObject
import javax.inject.Inject

class AuthenticationInteractor @Inject constructor(
    private val identityRemoteDataSource: IdentityRemoteDatasource,
    private val authSpStorage: AuthSpStorage
) {
    fun login(request: LoginRequest) = identityRemoteDataSource.login(request)

    fun saveAuthToken(
        loginResponse: LoginResponse
    ) {
        if (loginResponse.accessToken != null) {
            authSpStorage.accessToken = loginResponse.accessToken
        }
        if (loginResponse.refreshToken != null) {
            authSpStorage.refreshToken = loginResponse.refreshToken
        }
    }

    fun syncRefreshToken(request: LoginRequest) = identityRemoteDataSource.syncRefreshToken(request)
}