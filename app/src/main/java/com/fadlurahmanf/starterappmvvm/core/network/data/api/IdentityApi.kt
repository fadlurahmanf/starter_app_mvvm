package com.fadlurahmanf.starterappmvvm.core.network.data.api

import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.LoginRequest
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface IdentityApi {
    @POST("auth/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<BaseResponse<LoginResponse>>

    @POST("auth/refresh")
    fun syncRefreshToken(
        @Body body: RefreshTokenRequest
    ): Call<BaseResponse<LoginResponse>>

    @POST("auth/refresh")
    fun refreshToken(
        @Body body: RefreshTokenRequest,
    ): Observable<BaseResponse<LoginResponse>>
}