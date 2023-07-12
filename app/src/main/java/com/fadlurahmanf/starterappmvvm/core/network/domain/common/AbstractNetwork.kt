package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig

abstract class  AbstractNetwork<T>(context:Context): BaseNetwork<T>(context){
    override fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }
}
