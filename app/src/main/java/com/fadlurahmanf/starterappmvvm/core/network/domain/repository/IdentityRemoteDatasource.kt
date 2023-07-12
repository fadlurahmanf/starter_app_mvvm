package com.fadlurahmanf.starterappmvvm.core.network.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.network.data.api.IdentityApi
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.LoginRequest
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.core.network.domain.common.IdentityNetwork
import javax.inject.Inject

class IdentityRemoteDatasource @Inject constructor(
    context: Context
): IdentityNetwork<IdentityApi>(context) {
    override fun getApi(): Class<IdentityApi> {
        return IdentityApi::class.java
    }

    fun login(request: LoginRequest) = networkService(30).login(request)

    fun refreshToken(request: RefreshTokenRequest) = networkService(30).refreshToken(request)
}