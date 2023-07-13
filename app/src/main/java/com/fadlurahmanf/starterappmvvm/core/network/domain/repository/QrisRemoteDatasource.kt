package com.fadlurahmanf.starterappmvvm.core.network.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.network.data.api.TransactionApi
import com.fadlurahmanf.starterappmvvm.core.network.domain.common.AbstractCifNetwork
import com.fadlurahmanf.starterappmvvm.core.network.domain.common.AbstractTransactionNetwork
import com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.request.InquiryQrisRequest
import javax.inject.Inject

class QrisRemoteDatasource @Inject constructor(
    context: Context,
    cryptoAES: CryptoAES
) : AbstractTransactionNetwork<TransactionApi>(context, cryptoAES) {
    override fun getApi(): Class<TransactionApi> {
        return TransactionApi::class.java
    }

    fun inquiryQris(request: InquiryQrisRequest) = networkService(30).inquiryQris(request)
}