package com.fadlurahmanf.starterappmvvm.core.network.domain.interactor

import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.LoginRequest
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.domain.repository.IdentityRemoteDatasource
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.response.LoginResponse
import javax.inject.Inject

class AuthenticationInteractor @Inject constructor(
    private val identityRemoteDataSource: IdentityRemoteDatasource,
    private val authSpStorage: AuthSpStorage
) {
    fun login(request: LoginRequest) = identityRemoteDataSource.login(request)
    fun createGuestToken(request: CreateGuestTokenRequest) =
        identityRemoteDataSource.createGuestToken(request)

    fun saveAuthToken(
        loginResponse: LoginResponse
    ) {
        if (loginResponse.accessToken?.isNotEmpty() == true) {
            authSpStorage.accessToken = loginResponse.accessToken
        }
        if (loginResponse.refreshToken?.isNotEmpty() == true) {
            authSpStorage.refreshToken = loginResponse.refreshToken
        }
    }

    fun saveGuestToken(
        guestToken: String
    ) {
        if (guestToken.isNotEmpty()) {
            authSpStorage.guestToken = guestToken
        }
    }

    fun refreshToken(request: RefreshTokenRequest) = identityRemoteDataSource.refreshToken(request)
}