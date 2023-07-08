package com.fadlurahmanf.starterappmvvm.unknown.data.api.datasource.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.unknown.data.api.path.example.CIFApi
import com.fadlurahmanf.starterappmvvm.unknown.network.CIFNetwork
import javax.inject.Inject

class CIFDatasource @Inject constructor(
    context: Context
): CIFNetwork<CIFApi>(context) {
    override fun getApi(): Class<CIFApi> {
        return CIFApi::class.java
    }

    fun getFavorite() = networkService(30).getFavorite()
}