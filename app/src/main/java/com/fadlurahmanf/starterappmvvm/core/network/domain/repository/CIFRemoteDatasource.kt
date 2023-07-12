package com.fadlurahmanf.starterappmvvm.core.network.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.network.data.api.CIFApi
import com.fadlurahmanf.starterappmvvm.core.network.domain.common.CIFNetwork
import javax.inject.Inject

class CIFRemoteDatasource @Inject constructor(
    context: Context
): CIFNetwork<CIFApi>(context) {
    override fun getApi(): Class<CIFApi> {
        return CIFApi::class.java
    }

    fun getFavorite() = networkService(30).getFavorite()
}