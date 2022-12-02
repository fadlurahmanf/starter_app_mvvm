package com.fadlurahmanf.starterappmvvm.data.entity.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.base.network.AbstractNetwork
import com.fadlurahmanf.starterappmvvm.data.api.example.QuranAPI
import javax.inject.Inject

class QuranDatasource @Inject constructor(
    var context: Context
): AbstractNetwork<QuranAPI>() {
    override fun getApi(): Class<QuranAPI> {
        return QuranAPI::class.java
    }

    fun getSurahs(edition:String) = networkService(30).getSurahs(edition)

}