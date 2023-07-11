package com.fadlurahmanf.starterappmvvm.feature.language.domain.repository

import android.content.Context
import java.util.Locale

interface LanguageImpl {
    fun isSupportedLanguage(code: String): Boolean
    fun changeLanguage(context: Context, code: String)
    fun getCurrentLocale(context: Context): Locale
}