package com.fadlurahmanf.starterappmvvm.data.datasource.example

import android.content.Context
import android.util.Log
import com.fadlurahmanf.starterappmvvm.constant.LogKey
import com.fadlurahmanf.starterappmvvm.core.helper.ConnectivityHelper
import com.fadlurahmanf.starterappmvvm.network.AbstractNetwork
import com.fadlurahmanf.starterappmvvm.data.api.example.QuranAPI
import com.fadlurahmanf.starterappmvvm.data.room.datasource.QuranRoomDatasource
import com.fadlurahmanf.starterappmvvm.dto.response.example.BaseQuranResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahsResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class QuranRepository @Inject constructor(
    var context: Context,
    var quranDatasource: QuranDatasource,
    var quranRoomDatasource: QuranRoomDatasource
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

class QuranDatasource @Inject constructor(
    var context: Context
): AbstractNetwork<QuranAPI>() {
    override fun getApi(): Class<QuranAPI> {
        return QuranAPI::class.java
    }

    fun getSurahs(edition:String) = networkService(30).getSurahs(edition)

}