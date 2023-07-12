package com.fadlurahmanf.starterappmvvm.core.network.domain.common

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BuildConfig

abstract class AbstractQuranNetwork<T>(context: Context): BaseNetwork<T>(context) {

    override fun getBaseUrl(): String = BuildConfig.BASE_QURAN_URL

}