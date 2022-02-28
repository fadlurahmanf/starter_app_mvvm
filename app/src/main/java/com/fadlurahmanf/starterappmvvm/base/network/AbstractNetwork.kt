package com.fadlurahmanf.starterappmvvm.base.network

import com.bpp_app.module_payment_selector.data.interceptor.ContentTypeInterceptor
import com.fadlurahmanf.starterappmvvm.BuildConfig
import okhttp3.OkHttpClient

abstract class AbstractNetwork<T>(): BaseNetwork<T>() {

    override fun getBaseUrl(): String {
        return BuildConfig.BASE_DEV_URL
    }


}