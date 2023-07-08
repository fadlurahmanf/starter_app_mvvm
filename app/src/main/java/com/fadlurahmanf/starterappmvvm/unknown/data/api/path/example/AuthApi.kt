package com.fadlurahmanf.starterappmvvm.unknown.data.api.path.example

import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    fun login(
        @Body body:JsonObject
    ) : Observable<BaseResponse<LoginResponse>>

    @POST("auth/refresh")
    fun refreshToken(
        @Body body:JsonObject
    ) : Call<BaseResponse<LoginResponse>>
}