package com.fadlurahmanf.starterappmvvm.unknown.ui

import android.content.Intent
import androidx.camera.core.ExperimentalGetImage
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.network.presentation.ExampleLoginActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.feature.language.domain.interactor.LanguageInteractor
import com.fadlurahmanf.starterappmvvm.unknown.di.component.CoreComponent
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@ExperimentalGetImage
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    lateinit var component: CoreComponent

    @Inject
    lateinit var languageInteractor: LanguageInteractor

    override fun initSetup() {
        val type = BuildConfig.BUILD_TYPE
        binding.tvEnv.text = type

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            logConsole.d("FCM TOKEN: $it")
        }.addOnFailureListener {
            logConsole.d("ON FAILURE FCM TOKEN: ${it.message}")
        }

        val local = languageInteractor.getCurrentLocale(this)
        val storage = languageInteractor.getStorageLocale()
        if (local.language != storage.language) {
            languageInteractor.changeLanguage(this, storage.language)
            recreate()
            return
        }

        Timer().schedule(1) {
            val intent = Intent(this@SplashActivity, ExampleLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.coreComponent().create()
        component.inject(this)
    }
}