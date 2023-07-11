package com.fadlurahmanf.starterappmvvm.feature.language.domain.repository

import android.content.Context
import java.util.Locale

class LanguageRepositoryImpl : LanguageImpl {
    override fun isSupportedLanguage(code: String): Boolean {
        return (code == "en" || code == "in")
    }

    override fun changeLanguage(context: Context, code: String) {
        val configuration = context.resources.configuration
        val local = Locale(code)
        Locale.setDefault(local)
        configuration.locale = local
        configuration.setLayoutDirection(local)
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    override fun getCurrentLanguage(context: Context): Locale {
        val configuration = context.resources.configuration
        return configuration.locale
    }
}