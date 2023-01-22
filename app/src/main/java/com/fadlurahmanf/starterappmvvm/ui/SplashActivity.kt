package com.fadlurahmanf.starterappmvvm.ui

import android.content.Intent
import androidx.camera.core.ExperimentalGetImage
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.BuildConfig
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.helper.TranslationHelper
import com.fadlurahmanf.starterappmvvm.data.storage.example.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.di.component.CoreComponent
import com.fadlurahmanf.starterappmvvm.ui.example.activity.FirstExampleActivity
import com.fadlurahmanf.starterappmvvm.utils.logging.logd
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


@ExperimentalGetImage
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    lateinit var component:CoreComponent

    @Inject
    lateinit var languageSpStorage: LanguageSpStorage

    override fun initSetup() {
        val type = BuildConfig.BUILD_TYPE
        binding.tvEnv.text = type

        val local = TranslationHelper.getCurrentLocale(this)
        if(languageSpStorage.languageId == null){
            languageSpStorage.languageId = "en"
            TranslationHelper.changeLanguage(this, "en")
            recreate()
            return
        }else if(languageSpStorage.languageId != local.language){
            TranslationHelper.changeLanguage(this, languageSpStorage.languageId!!)
            recreate()
            return
        }

        Timer().schedule(3000){
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