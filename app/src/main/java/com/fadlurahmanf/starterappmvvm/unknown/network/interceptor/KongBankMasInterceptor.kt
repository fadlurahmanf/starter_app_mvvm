package com.fadlurahmanf.starterappmvvm.unknown.network.interceptor

import com.fadlurahmanf.starterappmvvm.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

open class KongBankMasInterceptor :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addHeader(chain.request()))
    }

    private fun addHeader(oriRequest: Request): Request {
        return oriRequest.newBuilder().addHeader("Authorization","Basic ${BuildConfig.KONG_BASIC_AUTH}").build()
    }
}