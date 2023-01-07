package com.fadlurahmanf.starterappmvvm.data.api.datasource.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.data.api.path.example.AuthApi
import com.fadlurahmanf.starterappmvvm.network.IdentityNetwork
import com.google.gson.JsonObject
import javax.inject.Inject

class IdentityDatasource @Inject constructor(
    context: Context
): IdentityNetwork<AuthApi>(context) {
    override fun getApi(): Class<AuthApi> {
        return AuthApi::class.java
    }

    fun login(body: JsonObject) = networkService(30).login(body)

    fun syncRefreshToken(body: JsonObject) = networkService(30).refreshToken(body)
}