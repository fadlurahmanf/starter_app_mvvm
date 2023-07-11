package com.fadlurahmanf.starterappmvvm.feature.language.domain.repository

class LanguageRepositoryImpl:LanguageImpl {
    override fun isSupportedLanguage(code: String): Boolean {
        return true
    }

    override fun changeLanguage(code: String) {

    }
}