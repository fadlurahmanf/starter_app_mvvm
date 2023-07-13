package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES

abstract class AbstractCifNetwork<T>(context: Context, cryptoAES: CryptoAES) :
    AbstractBankMasAfterLoginNetwork<T>(context, cryptoAES) {
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.CIF_PREFIX}"
    }
}

abstract class AbstractTransactionNetwork<T>(context: Context, cryptoAES: CryptoAES) :
    AbstractBankMasAfterLoginNetwork<T>(context, cryptoAES) {
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.TRANSACTION_PREFIX}"
    }
}