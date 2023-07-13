package com.fadlurahmanf.starterappmvvm.core.network.data.authenticator

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.network.data.api.IdentityApi
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.RefreshTokenRequest
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.BuildFlavorConstant
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import com.fadlurahmanf.starterappmvvm.core.network.data.interceptor.BankMasInterceptor
import com.fadlurahmanf.starterappmvvm.core.network.external.ChuckerHelper
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TokenAuthenticator(
    private val context: Context,
    private val cryptoAES: CryptoAES
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val request = response.request
        val authSpStorage = AuthSpStorage(context, cryptoAES)
        val refreshToken = authSpStorage.refreshToken
        if (refreshToken != null) {
            val refreshTokenRequest = RefreshTokenRequest(refreshToken)
            val refreshTokenResponse = identityApi().syncRefreshToken(refreshTokenRequest).execute()
            val body = refreshTokenResponse.body()
            if (refreshTokenResponse.isSuccessful && (body?.data?.accessToken?.isEmpty() == false || body?.data?.refreshToken?.isEmpty() == false)) {
                val accessToken = body.data?.accessToken
                val newRefreshToken = body.data?.refreshToken
                if (accessToken != null) {
                    authSpStorage.accessToken = accessToken
                }
                if (newRefreshToken != null) {
                    authSpStorage.refreshToken = newRefreshToken
                }
                val newRequest = request.newBuilder()
                newRequest.removeHeader("TESTING-REFRESH-TOKEN")
                newRequest.addHeader("TESTING-REFRESH-TOKEN", "false")
                newRequest.removeHeader("Authorization")
                newRequest.addHeader("Authorization", "Bearer $accessToken")
                return newRequest.build()
            } else {
                return request.newBuilder().build()
            }
        } else {
            return request.newBuilder().build()
        }
    }

    private val type = BuildConfig.BUILD_TYPE

    private fun client(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(BankMasInterceptor(context, cryptoAES))
        if (type != BuildFlavorConstant.production) {
            clientBuilder.addInterceptor(ChuckerHelper.provideInterceptor(context))
        }
        return clientBuilder
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun identityApi(): IdentityApi {
        val remoteConfig = (context.applicationContext as BaseApp).remoteConfig
        val baseUrl =
            if (remoteConfig.getString(AppConstant.RCK.TYPE_TOKEN) == AppConstant.RCV.GUEST) {
                BuildConfig.GUEST_URL
            } else {
                BuildConfig.BASIC_URL
            }
        return Retrofit.Builder()
            .baseUrl(baseUrl + BuildConfig.IDENTITY_PREFIX)
            .client(client())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(IdentityApi::class.java)
    }
}