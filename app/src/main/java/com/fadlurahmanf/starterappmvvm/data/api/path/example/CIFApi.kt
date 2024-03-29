package com.fadlurahmanf.starterappmvvm.data.api.path.example

import com.fadlurahmanf.starterappmvvm.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.FavoriteResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Headers

interface CIFApi {
    @Headers(value = [
        "Authorization: Bearer TES"
    ])
    @GET("favorite")
    fun getFavorite() : Observable<BaseResponse<List<FavoriteResponse>>>
}