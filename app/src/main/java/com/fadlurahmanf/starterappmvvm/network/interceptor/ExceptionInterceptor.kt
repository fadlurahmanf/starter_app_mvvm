package com.fadlurahmanf.starterappmvvm.network.interceptor

import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.constant.ExceptionConstant
import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException
import java.net.UnknownHostException

class ExceptionInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            return chain.proceed(request)
        }catch (e:Exception){
            when (e) {
                is HttpException -> {
                    throw CustomException()
                }
                is UnknownHostException -> {
                    throw CustomException(
                        rawMessage = ExceptionConstant.offline
                    )
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