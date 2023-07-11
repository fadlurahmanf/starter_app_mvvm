package com.fadlurahmanf.starterappmvvm.feature.language.domain.interactor

import android.content.Context
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.data.dto.event.RxEvent
import com.fadlurahmanf.starterappmvvm.core.data.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.core.external.helper.RxBus
import com.fadlurahmanf.starterappmvvm.feature.language.data.storage.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.feature.language.domain.repository.LanguageRepositoryImpl
import java.util.Locale
import javax.inject.Inject

class LanguageInteractor @Inject constructor(
    private val languageRepositoryImpl: LanguageRepositoryImpl,
    private val languageSpStorage: LanguageSpStorage
) {

    fun getCurrentLocale(context: Context): Locale {
        return languageRepositoryImpl.getCurrentLocale(context)
    }

    fun getStorageLocale(): Locale {
        val code = languageSpStorage.languageCode ?: "en"
        return Locale(code)
    }

    fun changeLanguage(context: Context, code: String) {
        if (languageRepositoryImpl.isSupportedLanguage(code)) {
            languageRepositoryImpl.changeLanguage(context, code)
            languageSpStorage.languageCode = code
            RxBus.publish(RxEvent.ChangeLanguageEvent(code))
        } else {
            throw CustomException(idRawMessage = R.string.exception_general)
        }
    }
}