package com.fadlurahmanf.starterappmvvm.network.authenticator

import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.constant.BuildFlavorConstant
import com.fadlurahmanf.starterappmvvm.constant.SpKey
import com.fadlurahmanf.starterappmvvm.data.api.path.example.AuthApi
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class TokenAuthenticator(
    private var context: Context
): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TokenAuthenticator", "authenticate: ${response.body?.string()}")
        val request = response.request

        val refreshToken = sharedPreference().getString(SpKey.REFRESH_TOKEN, "")
        val body = JsonObject()
        body.addProperty("token", refreshToken)
        val refreshTokenResponse = api().refreshToken(body).execute()

        return if (refreshTokenResponse.isSuccessful
            && refreshTokenResponse.body()?.code == "200"
            && refreshTokenResponse.body()?.data?.accessToken != null
            && refreshTokenResponse.body()?.data?.refreshToken != null){
            sharedPreference().edit().putString(SpKey.ACCESS_TOKEN, refreshTokenResponse.body()?.data?.accessToken).apply()
            sharedPreference().edit().putString(SpKey.REFRESH_TOKEN, refreshTokenResponse.body()?.data?.refreshToken).apply()
            request.newBuilder()
                .header("Authorization", "Bearer ${refreshTokenResponse.body()?.data?.accessToken}")
                .build()
        }else{
            request.newBuilder().build()
        }
    }

    private fun sharedPreference() = context
        .getSharedPreferences(SpKey.SP_KEY, Context.MODE_PRIVATE)

    private val type = BuildConfig.BUILD_TYPE
    private fun chuckerInterceptor(): ChuckerInterceptor {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = type != BuildFlavorConstant.dev,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor.Builder(context)
            .collector(chuckerCollector)
            .maxContentLength(Long.MAX_VALUE)
            .alwaysReadResponseBody(true)
            .build()
    }

    private fun client(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        if (type != BuildFlavorConstant.production){
            clientBuilder.addInterceptor(chuckerInterceptor())
        }
        return clientBuilder
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun api() = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL+BuildConfig.IDENTITY_PREFIX)
        .client(client())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(AuthApi::class.java)
}