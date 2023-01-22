package com.fadlurahmanf.starterappmvvm.data.api.datasource.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.data.api.path.example.CIFApi
import com.fadlurahmanf.starterappmvvm.network.CIFNetwork
import javax.inject.Inject

class CIFDatasource @Inject constructor(
    context: Context
): CIFNetwork<CIFApi>(context) {
    override fun getApi(): Class<CIFApi> {
        return CIFApi::class.java
    }

    fun getFavorite() = networkService(30).getFavorite()
}