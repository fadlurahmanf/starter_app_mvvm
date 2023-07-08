package com.fadlurahmanf.starterappmvvm

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.facebook.FacebookSdk
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repositories.LoggerRoomDatasource
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.usecases.LoggerImpl
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.LogConsole
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ApplicationComponent
import com.fadlurahmanf.starterappmvvm.unknown.di.component.DaggerApplicationComponent
import com.fadlurahmanf.starterappmvvm.unknown.utils.logging.logd

class BaseApp : Application() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var logConsole: LogConsole

    override fun onCreate() {
        super.onCreate()
        initInjection()
        setupLogConsole()
        setupAppsFlyer()
    }

    private fun setupLogConsole() {
        logConsole = LogConsole(
            loggerImpl = LoggerImpl(
                loggerRoomDatasource = LoggerRoomDatasource(applicationContext)
            )
        )
    }

    private fun setupAppsFlyer() {
        if (BuildConfig.BUILD_TYPE.contains("debug", ignoreCase = true)) {
            AppsFlyerLib.getInstance().setDebugLog(true)
            FacebookSdk.setIsDebugEnabled(true)
        } else {
            AppsFlyerLib.getInstance().setDebugLog(false)
        }
        AppsFlyerLib.getInstance().setDebugLog(false)
        AppsFlyerLib.getInstance()
            .init("fbhqQESGV9HB8LsHc4p2k4", object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    logd("APPSFLYER -> onConversionDataSuccess")
                }

                override fun onConversionDataFail(p0: String?) {
                    logd("APPSFLYER -> onConversionDataFail: $p0")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    logd("APPSFLYER -> onAppOpenAttribution")
                }

                override fun onAttributionFailure(p0: String?) {
                    logd("APPSFLYER -> onAttributionFailure: $p0")
                }

            }, this)
        AppsFlyerLib.getInstance()
            .start(this, "fbhqQESGV9HB8LsHc4p2k4", object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    logd("APPSFLYER -> onSuccess start appsflyer")
                }

                override fun onError(p0: Int, p1: String) {
                    logd("APPSFLYER -> onError start appsflyer CODE: $p0, MESSAGE: $p1")
                }
            })
    }

    private fun initInjection() {
        applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.inject(this)
    }
}