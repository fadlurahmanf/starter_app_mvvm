package com.fadlurahmanf.starterappmvvm.core.network.data.api

import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

interface CIFApi {
    @GET("favorite")
    fun getFavorite(
        @Header("TESTING-REFRESH-TOKEN") tesHeader:String
    ) : Observable<BaseResponse<List<FavoriteResponse>>>
}