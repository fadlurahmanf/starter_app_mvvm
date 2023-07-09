package com.fadlurahmanf.starterappmvvm.unknown.data.repository.example

import android.content.Context
import android.util.Log
import com.fadlurahmanf.starterappmvvm.core.data.constant.LogKey
import com.fadlurahmanf.starterappmvvm.core.external.helper.ConnectivityHelper
import com.fadlurahmanf.starterappmvvm.unknown.data.api.datasource.example.QuranDatasource
import com.fadlurahmanf.starterappmvvm.unknown.data.room.datasource.QuranRoomDatasource
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.BaseQuranResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahsResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class QuranRepository @Inject constructor(
    private var context: Context,
    private var quranDatasource: QuranDatasource,
    private var quranRoomDatasource: QuranRoomDatasource
){
    fun getSurahs(edition: String):Observable<BaseQuranResponse<SurahsResponse>>{
        return if(ConnectivityHelper.isNetworkAvailable(context)){
            getSurahsFromApi(edition)
        }else{
            getSurahsFromDb()
        }
    }

    private fun getSurahsFromApi(edition: String):Observable<BaseQuranResponse<SurahsResponse>>{
        return quranDatasource.getSurahs(edition)
            .doOnNext {
                Log.d(LogKey.DEBUG, "getSurahsFromApi")
                if(it.code == 200 && it.status == "OK" && it.data?.surahs != null){
                    Log.d(LogKey.DEBUG, "insert all surah to room")

                    quranRoomDatasource.insertAll(it.data!!.surahs)
                }
            }
    }

    private fun getSurahsFromDb():Observable<BaseQuranResponse<SurahsResponse>>{
        return quranRoomDatasource.getAll().toObservable()
            .map {
                BaseQuranResponse(
                    code = 200,
                    status = "OK",
                    data = SurahsResponse(
                        surahs = it
                    )
                )
            }.doOnNext {
                Log.d(LogKey.DEBUG, "getSurahsFromDb")
            }

    }
}