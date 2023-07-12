package com.fadlurahmanf.starterappmvvm.core.network.data.api

import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.LoginRequest
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IdentityApi {
    @POST("auth/login")
    fun login(
        @Body body:LoginRequest
    ) : Observable<BaseResponse<LoginResponse>>

    @POST("auth/refresh")
    fun refreshToken(
        @Body body:LoginRequest
    ) : Call<BaseResponse<LoginResponse>>

    @POST("auth/refresh")
    fun refreshToken2(
        @Body body:JsonObject
    ) : Call<BaseResponse<LoginResponse>>
}