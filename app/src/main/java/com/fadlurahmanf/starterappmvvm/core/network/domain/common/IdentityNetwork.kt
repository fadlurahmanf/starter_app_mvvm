package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES

abstract class IdentityNetwork<T>(context: Context, cryptoAES: CryptoAES) : BankMasNetwork<T>(context, cryptoAES) {
    override fun getBaseUrl(): String {
        return "${BuildConfig.BASE_URL}${BuildConfig.IDENTITY_PREFIX}"
    }
}