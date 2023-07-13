package com.fadlurahmanf.starterappmvvm.core.network.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.network.data.api.CIFApi
import com.fadlurahmanf.starterappmvvm.core.network.domain.common.AbstractCifNetwork
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CifRemoteDatasource @Inject constructor(
    context: Context,
    cryptoAES: CryptoAES
) : AbstractCifNetwork<CIFApi>(context, cryptoAES) {
    override fun getApi(): Class<CIFApi> {
        return CIFApi::class.java
    }

    fun getFavorite(debugForceRefreshToken: Boolean): Observable<BaseResponse<List<FavoriteResponse>>> {
        return networkService(30).getFavorite(if (debugForceRefreshToken) "true" else "false")
    }
}