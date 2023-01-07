package com.fadlurahmanf.starterappmvvm.data.api.datasource.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.data.api.path.example.QuranAPI
import com.fadlurahmanf.starterappmvvm.network.AbstractQuranNetwork
import javax.inject.Inject

class QuranDatasource @Inject constructor(
    context: Context
): AbstractQuranNetwork<QuranAPI>(context) {
    override fun getApi(): Class<QuranAPI> {
        return QuranAPI::class.java
    }

    fun getSurahs(edition:String) = networkService(30).getSurahs(edition)

}