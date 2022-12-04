package com.fadlurahmanf.starterappmvvm.network

import com.fadlurahmanf.starterappmvvm.BuildConfig

abstract class  AuthAbstractNetwork<T>(): BaseNetwork<T>(){
    override fun getBaseUrl(): String {
        return when (BuildConfig.BUILD_TYPE) {
            "release" -> BuildConfig.BASE_PRODUCTION_URL
            "staging" -> BuildConfig.BASE_STAGING_URL
            else -> BuildConfig.BASE_DEV_URL
        }
    }
}

abstract class AbstractNetwork<T>(): BaseNetwork<T>() {

    override fun getBaseUrl(): String {
        return when (BuildConfig.BUILD_TYPE) {
            "release" -> BuildConfig.BASE_PRODUCTION_URL
            "staging" -> BuildConfig.BASE_STAGING_URL
            else -> BuildConfig.BASE_DEV_URL
        }
    }


}