package com.fadlurahmanf.starterappmvvm.feature.language.domain.repository

interface LanguageImpl {
    fun isSupportedLanguage(code:String):Boolean
    fun changeLanguage(code: String)
}