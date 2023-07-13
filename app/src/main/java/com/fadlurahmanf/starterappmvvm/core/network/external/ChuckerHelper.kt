package com.fadlurahmanf.starterappmvvm.core.network.external

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.BuildFlavorConstant

class ChuckerHelper {
    companion object {
        fun provideInterceptor(context: Context): ChuckerInterceptor {
            val type = BuildConfig.FLAVOR
            val chuckerCollector = ChuckerCollector(
                context = context,
                showNotification = type != BuildFlavorConstant.production,
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )

            return ChuckerInterceptor.Builder(context)
                .collector(chuckerCollector)
                .maxContentLength(Long.MAX_VALUE)
                .alwaysReadResponseBody(true)
                .build()
        }
    }
}