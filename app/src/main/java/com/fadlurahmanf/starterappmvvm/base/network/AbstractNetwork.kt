package com.fadlurahmanf.starterappmvvm.base.network

import com.fadlurahmanf.starterappmvvm.BuildConfig

abstract class  AuthAbstractNetwork<T>():BaseNetwork<T>(){
    override fun getBaseUrl(): String {
        return when (BuildConfig.ENV) {
            "PRODUCTION" -> BuildConfig.BASE_PRODUCTION_URL
            "STAGING" -> BuildConfig.BASE_STAGING_URL
            else -> BuildConfig.BASE_DEV_URL
        }
    }
}

abstract class AbstractNetwork<T>(): BaseNetwork<T>() {

    override fun getBaseUrl(): String {
        return when (BuildConfig.ENV) {
            "PRODUCTION" -> BuildConfig.BASE_PRODUCTION_URL
            "STAGING" -> BuildConfig.BASE_STAGING_URL
            else -> BuildConfig.BASE_DEV_URL
        }
    }


}