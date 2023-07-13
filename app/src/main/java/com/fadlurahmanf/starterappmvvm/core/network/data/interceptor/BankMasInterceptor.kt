package com.fadlurahmanf.starterappmvvm.core.network.data.interceptor

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

open class BankMasInterceptor(private val context: Context, private val cryptoAES: CryptoAES) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addHeader(chain.request()))
    }

    private fun addHeader(oriRequest: Request): Request {
        val request = oriRequest.newBuilder()
        val remoteConfig = (context.applicationContext as BaseApp).remoteConfig
        if (remoteConfig.getString(AppConstant.RCK.TYPE_TOKEN) == AppConstant.RCV.GUEST) {
            val authSpStorage = AuthSpStorage(context, cryptoAES)
            request.addHeader("Authorization", "Bearer ${authSpStorage.guestToken}")
        } else {
            request.addHeader("Authorization", "Basic ${BuildConfig.KONG_BASIC_AUTH}")
        }
        return request.build()
    }
}