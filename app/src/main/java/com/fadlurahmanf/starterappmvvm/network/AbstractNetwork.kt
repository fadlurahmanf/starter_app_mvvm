package com.fadlurahmanf.starterappmvvm.network

import com.fadlurahmanf.starterappmvvm.BuildConfig
import okhttp3.OkHttpClient

abstract class IdentityNetwork<T>():AbstractNetwork<T>(){
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.IDENTITY_PREFIX}"
    }
}

abstract class  AuthAbstractNetwork<T>(): BaseNetwork<T>(){
    override fun getBaseUrl(): String {
        return when (BuildConfig.BUILD_TYPE) {
            "release" -> BuildConfig.BASE_PRODUCTION_URL
            "staging" -> BuildConfig.BASE_STAGING_URL
            else -> BuildConfig.BASE_DEV_URL
        }
    }

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.authenticator(TokenAuthenticator())
    }
}

abstract class  AbstractNetwork<T>(): BaseNetwork<T>(){
    override fun getBaseUrl(): String {
        return when (BuildConfig.BUILD_TYPE) {
            "release" -> BuildConfig.BASE_PRODUCTION_URL
            "staging" -> BuildConfig.BASE_STAGING_URL
            else -> BuildConfig.BASE_DEV_URL
        }
    }
}

abstract class AbstractQuranNetwork<T>(): BaseNetwork<T>() {

    override fun getBaseUrl(): String = BuildConfig.BASE_QURAN_URL

}