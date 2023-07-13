package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.network.data.authenticator.TokenAuthenticator
import com.fadlurahmanf.starterappmvvm.core.network.data.interceptor.BankMasAfterLoginInterceptor
import okhttp3.OkHttpClient

/**
 * Abstract network bank mas after login
 **/
abstract class BankMasAfterLoginNetwork<T>(context: Context, private val cryptoAES: CryptoAES) :
    BankMasNetwork<T>(context, cryptoAES) {
    override fun getBaseUrl(): String {
        return BuildConfig.BASIC_URL
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BankMasAfterLoginInterceptor(context, cryptoAES))
            .authenticator(TokenAuthenticator(context, cryptoAES))
    }
}