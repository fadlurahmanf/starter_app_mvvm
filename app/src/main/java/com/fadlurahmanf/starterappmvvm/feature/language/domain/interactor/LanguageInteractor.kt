package com.fadlurahmanf.starterappmvvm.feature.language.domain.interactor

import android.content.Context
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.feature.language.domain.repository.LanguageRepositoryImpl
import javax.inject.Inject

class LanguageInteractor @Inject constructor(
    private var context: Context,
    private var languageRepositoryImpl: LanguageRepositoryImpl
) {
    fun changeLanguage(code: String) {
        if (languageRepositoryImpl.isSupportedLanguage(code)) {
            languageRepositoryImpl.changeLanguage(context, code)
        } else {
            throw CustomException(idRawMessage = R.string.exception_general)
        }
    }
}