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

    private fun certificatePinner(): CertificatePinner {
        return CertificatePinner
            .Builder()
            .add("guest.bankmas.my.id", "sha256/wWNGHPC/VLetRp8oYOfMA5OKj1BIbXsrHHvvC/zhdYg=")
            .add("guest.bankmas.my.id", "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
            .add("guest.bankmas.my.id", "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
            .add("api.bankmas.my.id", "sha256//tIqWQyKa1eSahlaRjGHRPSqjm60JN/EI+ZiJBUbjG8=")
            .add("api.bankmas.my.id", "sha256/jQJTbIh0grw0/1TkHSumWb+Fs0Ggogr621gT3PvPKG0=")
            .add("api.bankmas.my.id", "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M=")
            .build()
    }

    open fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val p0 = builder
            .certificatePinner(certificatePinner())
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