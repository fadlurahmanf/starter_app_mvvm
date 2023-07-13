package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.network.data.interceptor.ExceptionInterceptor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.BuildFlavorConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseNetwork<T>(var context: Context) {

    var service: T? = null

    private fun bodyLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val type: String = BuildConfig.FLAVOR
    private fun chuckerInterceptor(): ChuckerInterceptor {

        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = type != BuildFlavorConstant.production,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(Long.MAX_VALUE)
            .alwaysReadResponseBody(true)
            .build()
    }

    open fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val p0 = builder.addNetworkInterceptor(bodyLoggingInterceptor())
        if (type != BuildFlavorConstant.production) {
            p0.addInterceptor(chuckerInterceptor())
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