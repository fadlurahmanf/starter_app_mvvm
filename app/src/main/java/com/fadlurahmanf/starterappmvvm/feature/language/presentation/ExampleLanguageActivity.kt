package com.fadlurahmanf.starterappmvvm.feature.language.presentation

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.AnalyticEvent
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleLanguageBinding
import com.fadlurahmanf.starterappmvvm.feature.language.domain.interactor.LanguageInteractor
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.utils.analytic.AnalyticHelper
import javax.inject.Inject

class ExampleLanguageActivity :
    BaseActivity<ActivityExampleLanguageBinding>(ActivityExampleLanguageBinding::inflate) {

    @Inject
    lateinit var languageInteractor: LanguageInteractor

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        println("MASUK onConfigurationChanged")
    }

    override fun initSetup() {
        binding.btnChangeLanguage.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_change_language)
            val local = languageInteractor.getCurrentLocale(this)
            if (local.language == "en") {
                languageInteractor.changeLanguage(this, "in")
            } else {
                languageInteractor.changeLanguage(this, "en")
            }
            recreate()
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}