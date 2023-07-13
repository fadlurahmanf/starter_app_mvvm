package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.network.data.interceptor.BankMasInterceptor
import okhttp3.OkHttpClient

abstract class BankMasNetwork<T>(context: Context, private val cryptoAES: CryptoAES) :
    BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BankMasInterceptor())
    }
}