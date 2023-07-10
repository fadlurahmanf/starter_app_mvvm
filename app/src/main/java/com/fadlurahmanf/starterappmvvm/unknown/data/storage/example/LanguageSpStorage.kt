package com.fadlurahmanf.starterappmvvm.unknown.data.storage.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.feature.sp.domain.common.BasePreference
import com.fadlurahmanf.starterappmvvm.core.data.constant.SpKey
import javax.inject.Inject

class LanguageSpStorage @Inject constructor(
    context: Context
) : BasePreference(context) {
    var languageId:String?=null
    get() {
        return getString(SpKey.LANGUAGE_ID)
    }set(value) {
        field = if (value != null){
            saveString(SpKey.LANGUAGE_ID, value)
            value
        }else{
            clearData(SpKey.LANGUAGE_ID)
            null
        }
    }
}