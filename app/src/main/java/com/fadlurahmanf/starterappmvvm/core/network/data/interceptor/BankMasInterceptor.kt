package com.fadlurahmanf.starterappmvvm.core.network.data.interceptor

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

open class BankMasInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addHeader(chain.request()))
    }

    private fun addHeader(oriRequest: Request): Request {
        val request = oriRequest.newBuilder()
        request.addHeader("Authorization", "Basic ${BuildConfig.KONG_BASIC_AUTH}")
        return request.build()
    }
}