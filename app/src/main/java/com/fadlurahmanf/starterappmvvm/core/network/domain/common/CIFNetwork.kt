package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig

abstract class CIFNetwork<T>(context: Context): AuthAbstractNetwork<T>(context){
    override fun getBaseUrl(): String {
        return "${super.getBaseUrl()}${BuildConfig.CIF_PREFIX}"
    }
}