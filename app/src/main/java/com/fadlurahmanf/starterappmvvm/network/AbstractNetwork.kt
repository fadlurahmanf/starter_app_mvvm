package com.fadlurahmanf.starterappmvvm.network

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.network.authenticator.TokenAuthenticator
import okhttp3.OkHttpClient

abstract class IdentityNetwork<T>():AbstractNetwork<T>(){
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.IDENTITY_PREFIX}"
    }
}

abstract class CIFNetwork<T>(context: Context):AuthAbstractNetwork<T>(context){
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.CIF_PREFIX}"
    }
}

abstract class  AuthAbstractNetwork<T>(
    var context: Context
): BaseNetwork<T>(){
    override fun getBaseUrl(): String = BuildConfig.BASE_URL

    override fun okHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder.authenticator(TokenAuthenticator(context))
    }
}

abstract class  AbstractNetwork<T>(): BaseNetwork<T>(){
    override fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}

abstract class AbstractQuranNetwork<T>(): BaseNetwork<T>() {

    override fun getBaseUrl(): String = BuildConfig.BASE_QURAN_URL

}