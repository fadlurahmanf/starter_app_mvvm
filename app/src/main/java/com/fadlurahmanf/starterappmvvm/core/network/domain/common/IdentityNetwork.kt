package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig

abstract class IdentityNetwork<T>(context: Context) : BankMasNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return "${BuildConfig.BASE_URL}${BuildConfig.IDENTITY_PREFIX}"
    }
}