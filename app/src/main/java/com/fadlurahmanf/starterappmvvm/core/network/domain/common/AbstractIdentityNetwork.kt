package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES

abstract class AbstractIdentityNetwork<T>(context: Context, cryptoAES: CryptoAES) : AbstractBankMasNetwork<T>(context, cryptoAES) {
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.IDENTITY_PREFIX}"
    }
}