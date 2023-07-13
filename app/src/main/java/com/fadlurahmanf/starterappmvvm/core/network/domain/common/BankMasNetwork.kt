package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.network.data.interceptor.BankMasInterceptor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient

/**
 * Abstract network bank mas before login
 **/
abstract class BankMasNetwork<T>(context: Context, private val cryptoAES: CryptoAES) :
    BaseNetwork<T>(context) {
    override fun getBaseUrl(): String {
        // checking if remote config is guest or basic
        val remoteConfig = (this.context.applicationContext as BaseApp).remoteConfig
        return if (remoteConfig.getString(AppConstant.RCK.TYPE_TOKEN) == AppConstant.RCV.GUEST) {
            BuildConfig.GUEST_URL
        } else {
            BuildConfig.BASIC_URL
        }
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return super.okHttpClientBuilder(builder)
            .addInterceptor(BankMasInterceptor(context, cryptoAES))
            .certificatePinner(certificatePinner())
    }

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
}