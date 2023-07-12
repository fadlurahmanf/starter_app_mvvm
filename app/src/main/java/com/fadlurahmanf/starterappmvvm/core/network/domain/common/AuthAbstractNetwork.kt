package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.unknown.network.authenticator.TokenAuthenticator
import okhttp3.OkHttpClient

abstract class  AuthAbstractNetwork<T>(
    context: Context
): BaseNetwork<T>(context){
    override fun getBaseUrl(): String = BuildConfig.BASE_URL

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .authenticator(TokenAuthenticator(context))
    }
}