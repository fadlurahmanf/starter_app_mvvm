package com.fadlurahmanf.starterappmvvm.feature.language.data.storage

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoRSA
import com.fadlurahmanf.starterappmvvm.core.sp.data.constant.SpConstant
import com.fadlurahmanf.starterappmvvm.core.sp.domain.common.BasePreference
import javax.inject.Inject

class LanguageSpStorage @Inject constructor(
    context: Context,
    cryptoAES: CryptoAES,
) : BasePreference(context, cryptoAES) {
    var languageCode: String? = null
        get() {
            return getString(SpConstant.LANGUAGE_CODE)
        }
        set(value) {
            field = if (value != null) {
                saveString(SpConstant.LANGUAGE_CODE, value)
                value
            } else {
                clearData(SpConstant.LANGUAGE_CODE)
                null
            }
        }
}