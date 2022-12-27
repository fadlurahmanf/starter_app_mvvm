package com.fadlurahmanf.starterappmvvm.data.api.datasource.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.data.api.path.example.QuranAPI
import com.fadlurahmanf.starterappmvvm.network.AbstractQuranNetwork
import javax.inject.Inject

class QuranDatasource @Inject constructor(
    var context: Context
): AbstractQuranNetwork<QuranAPI>() {
    override fun getApi(): Class<QuranAPI> {
        return QuranAPI::class.java
    }

    fun getSurahs(edition:String) = networkService(30).getSurahs(edition)

}