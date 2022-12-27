package com.fadlurahmanf.starterappmvvm.data.api.path.example

import com.fadlurahmanf.starterappmvvm.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.LoginResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    fun login(
        @Body body:JsonObject
    ) : Observable<BaseResponse<LoginResponse>>
}