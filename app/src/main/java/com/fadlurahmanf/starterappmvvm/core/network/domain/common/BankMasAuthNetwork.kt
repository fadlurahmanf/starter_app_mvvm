package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.unknown.network.authenticator.TokenAuthenticator
import com.fadlurahmanf.starterappmvvm.unknown.network.interceptor.BankMasAfterLoginInterceptor
import com.fadlurahmanf.starterappmvvm.unknown.network.interceptor.BankMasInterceptor
import okhttp3.OkHttpClient

abstract class BankMasAuthNetwork<T>(context: Context, private val cryptoAES: CryptoAES) :
    BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BankMasAfterLoginInterceptor(context, cryptoAES))
            .authenticator(TokenAuthenticator(context, cryptoAES))
    }
}