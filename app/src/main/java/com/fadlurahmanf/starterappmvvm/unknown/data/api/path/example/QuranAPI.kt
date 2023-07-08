package com.fadlurahmanf.starterappmvvm.unknown.data.api.path.example

import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.BaseQuranResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahsResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranAPI {
    @GET("quran/{edition}")
    fun getSurahs(
        @Path("edition") edition:String
    ): Observable<BaseQuranResponse<SurahsResponse>>
}