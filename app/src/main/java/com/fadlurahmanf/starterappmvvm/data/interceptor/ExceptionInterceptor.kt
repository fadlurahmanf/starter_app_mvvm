package com.fadlurahmanf.starterappmvvm.data.interceptor

import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException
import okhttp3.Interceptor
import okhttp3.Response
import java.net.UnknownHostException

class ExceptionInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            return chain.proceed(request)
        }catch (e:Exception){
            if(e is UnknownHostException){
                R.string.exception_offline;
                throw CustomException(
                    rawMessage = "exception_offline",
                    rawMessageId = R.string.exception_offline
                )
            }else{
                throw CustomException(
                    rawMessage = e.message
                )
            }
        }
    }
}