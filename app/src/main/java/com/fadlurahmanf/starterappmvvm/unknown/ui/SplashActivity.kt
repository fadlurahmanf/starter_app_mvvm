package com.fadlurahmanf.starterappmvvm.unknown.ui

import android.content.Intent
import android.content.pm.PackageManager
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.unknown.utils.language.TranslationHelper
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.CoreComponent
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.FirstExampleActivity
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.createNewLoggerFile
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.logd
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@ExperimentalGetImage
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    lateinit var component: CoreComponent

    @Inject
    lateinit var languageSpStorage: LanguageSpStorage

    override fun initSetup() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            println("MASUK MASUK SINI GRANTED")
            createNewLoggerFile()
        } else {
            println("GA GRANTED NIH")
        }
        PackageManager.PERMISSION_GRANTED
        createNewLoggerFile()
        val type = BuildConfig.BUILD_TYPE
        binding.tvEnv.text = type

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            logd("FCM TOKEN: $it")
        }.addOnFailureListener {
            logd("ON FAILURE FCM TOKEN: ${it.message}")
        }

        val local = TranslationHelper.getCurrentLocale(this)
        if (languageSpStorage.languageId == null) {
            languageSpStorage.languageId = "en"
            TranslationHelper.changeLanguage(this, "en")
            recreate()
            return
        } else if (languageSpStorage.languageId != local.language) {
            TranslationHelper.changeLanguage(this, languageSpStorage.languageId!!)
            recreate()
            return
        }

        Timer().schedule(3000) {
            val intent = Intent(this@SplashActivity, FirstExampleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.coreComponent().create()
        component.inject(this)
    }
}