package com.fadlurahmanf.starterappmvvm

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.facebook.FacebookSdk
import com.fadlurahmanf.starterappmvvm.core.domain.usecase.NotificationImpl
import com.fadlurahmanf.starterappmvvm.core.external.constant.logConsole
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repositories.LoggerRoomDatasource
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.usecases.LoggerImpl
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.LogConsole
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ApplicationComponent
import com.fadlurahmanf.starterappmvvm.unknown.di.component.DaggerApplicationComponent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class BaseApp : Application() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var remoteConfig:FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        initInjection()
        setupLogConsole()
        setupAppsFlyer()
        initRemoteConfigFirebase()
    }
    private fun initRemoteConfigFirebase(){
        remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .setFetchTimeoutInSeconds(60)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_firebase_configuration)

        remoteConfig.fetchAndActivate().addOnSuccessListener {
            logConsole.d("SUCCESS ACTIVATE REMOTE CONFIG")
        }.addOnFailureListener {
            logConsole.w("FAILED TO ACTIVATE REMOTE CONFIG: ${it.message}")
        }
    }

    private fun setupLogConsole() {
        logConsole = LogConsole(
            loggerImpl = LoggerImpl(LoggerRoomDatasource(applicationContext)),
            notificationImpl = NotificationImpl(applicationContext)
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
                    logConsole.d("APPSFLYER -> onConversionDataSuccess")
                }

                override fun onConversionDataFail(p0: String?) {
                    logConsole.d("APPSFLYER -> onConversionDataFail: $p0")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    logConsole.d("APPSFLYER -> onAppOpenAttribution")
                }

                override fun onAttributionFailure(p0: String?) {
                    logConsole.d("APPSFLYER -> onAttributionFailure: $p0")
                }

            }, this)
        AppsFlyerLib.getInstance()
            .start(this, "fbhqQESGV9HB8LsHc4p2k4", object : AppsFlyerRequestListener {
                override fun onSuccess() {
                    logConsole.d("APPSFLYER -> onSuccess start appsflyer")
                }

                override fun onError(p0: Int, p1: String) {
                    logConsole.d("APPSFLYER -> onError start appsflyer CODE: $p0, MESSAGE: $p1")
                }
            })
    }

    private fun initInjection() {
        applicationComponent = DaggerApplicationComponent.factory().create(this)
        applicationComponent.inject(this)
    }
}