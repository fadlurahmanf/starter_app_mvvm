package com.fadlurahmanf.starterappmvvm.data.api.path.example

import com.fadlurahmanf.starterappmvvm.dto.response.example.BaseQuranResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranAPI {
    @GET("quran/{edition}")
    fun getSurahs(
        @Path("edition") edition:String
    ): Observable<BaseQuranResponse<SurahsResponse>>
}