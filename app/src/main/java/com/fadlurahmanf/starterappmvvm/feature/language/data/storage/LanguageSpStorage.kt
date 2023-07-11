package com.fadlurahmanf.starterappmvvm.feature.language.data.storage

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.feature.sp.domain.common.BasePreference
import javax.inject.Inject

class LanguageSpStorage @Inject constructor(
    context: Context
) : BasePreference(context) {
    var languageCode: String? = null
        get() {
            return getString(AppConstant.Sp.LANGUAGE_CODE)
        }
        set(value) {
            field = if (value != null) {
                saveString(AppConstant.Sp.LANGUAGE_CODE, value)
                value
            } else {
                clearData(AppConstant.Sp.LANGUAGE_CODE)
                null
            }
        }
}