package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.network.data.interceptor.ExceptionInterceptor
import com.fadlurahmanf.starterappmvvm.core.network.external.ChuckerHelper
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.BuildFlavorConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseNetwork<T>(var context: Context) {

    var service: T? = null

    private fun bodyLoggingInterceptor(): HttpLoggingInterceptor {
        logConsole.d("MASUK bodyLoggingInterceptor")
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val type: String = BuildConfig.FLAVOR

    open fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val p0 = builder
            .addNetworkInterceptor(bodyLoggingInterceptor())
        if (type != BuildFlavorConstant.production) {
            p0.addInterceptor(ChuckerHelper.provideInterceptor(context))
        }
        return p0.addInterceptor(ExceptionInterceptor())
    }

    private fun provideClient(timeOut: Long): OkHttpClient {
        return okHttpClientBuilder(OkHttpClient.Builder())
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .build()
    }


    private fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
    }

    private fun provideRetrofit(timeOut: Long): Retrofit {
        return providesRetrofitBuilder().baseUrl(getBaseUrl())
            .client(provideClient(timeOut))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    open fun networkService(timeOut: Long): T {
        val retrofit = provideRetrofit(timeOut)
        service = retrofit.create(getApi())
        return service!!
    }

    abstract fun getApi(): Class<T>

    abstract fun getBaseUrl(): String
}