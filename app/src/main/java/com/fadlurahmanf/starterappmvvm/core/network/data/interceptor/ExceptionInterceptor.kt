package com.fadlurahmanf.starterappmvvm.core.network.data.interceptor

import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.ExceptionConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.exception.CustomException
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

class ExceptionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code == 401) {
                throw CustomException(
                    rawMessage = ExceptionConstant.unauthorized
                )
            }
            return response
        } catch (e: Throwable) {
            logConsole.e("ERROR ExceptionInterceptor: ${e.message}")
            when (e) {
                is HttpException -> {
                    throw CustomException()
                }

                is UnknownHostException -> {
                    throw CustomException(
                        rawMessage = ExceptionConstant.offline
                    )
                }

                is CustomException -> {
                    throw e
                }

                is SSLPeerUnverifiedException -> {
                    throw e
                }

                else -> {
                    throw CustomException(
                        rawMessage = e.message
                    )
                }
            }
        }
    }
}