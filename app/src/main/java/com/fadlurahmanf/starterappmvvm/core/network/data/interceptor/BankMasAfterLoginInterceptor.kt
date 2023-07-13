package com.fadlurahmanf.starterappmvvm.core.network.data.interceptor

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

open class BankMasAfterLoginInterceptor(
    private val context: Context,
    private val cryptoAES: CryptoAES,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(addHeader(chain.request()))
    }

    private fun addHeader(oriRequest: Request): Request {
        val authSpStorage = AuthSpStorage(context, cryptoAES)
        val accessToken = authSpStorage.accessToken
        val headers = hashMapOf<String, String>()
        headers["Authorization"] = "Bearer $accessToken"
        oriRequest.headers.forEach {
            headers[it.first] = it.second
        }
        val newRequest = oriRequest.newBuilder()
        headers.forEach { pair ->
            newRequest.removeHeader(pair.key)
            newRequest.addHeader(pair.key, pair.value)
        }
        if (headers["TESTING-REFRESH-TOKEN"] == "true") {
            newRequest.removeHeader("Authorization")
            newRequest.addHeader("Authorization", "Bearer DEBUG_TOKEN")
        }
        return newRequest.build()
    }
}