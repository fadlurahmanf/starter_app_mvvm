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
            val response = chain.proceed(request)
            if (response.code == 401){
                throw CustomException(
                    rawMessage = ExceptionConstant.unauthorized
                )
            }
            return response
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
                is CustomException -> {
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