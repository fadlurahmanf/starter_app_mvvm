package com.fadlurahmanf.starterappmvvm.feature.language.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.constant.AnalyticEvent
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleLanguageBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.utils.analytic.AnalyticHelper

class ExampleLanguageActivity : BaseActivity<ActivityExampleLanguageBinding>(ActivityExampleLanguageBinding::inflate) {
    override fun initSetup() {
        binding.btnChangeLanguage.setOnClickListener {
            AnalyticHelper.logEvent(this, AnalyticEvent.btn_change_language)
            val local = TranslationHelper.getCurrentLocale(this)
            if (local.language == "en") {
                TranslationHelper.changeLanguage(this, "in")
//                languageSpStorage.languageId = "in"
            } else {
                TranslationHelper.changeLanguage(this, "en")
//                languageSpStorage.languageId = "in"
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