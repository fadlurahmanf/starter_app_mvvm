package com.fadlurahmanf.starterappmvvm.unknown.data.storage.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.base.BasePreference
import com.fadlurahmanf.starterappmvvm.core.constant.SpKey
import javax.inject.Inject

class AuthSpStorage @Inject constructor(
    var context: Context
): BasePreference(context) {
    var accessToken:String? = null
    get() {
        field = getString(SpKey.ACCESS_TOKEN)
        return field
    }set(value) {
        field = if (value != null){
            saveString(SpKey.ACCESS_TOKEN, value)
            value
        }else{
            clearData(SpKey.ACCESS_TOKEN)
            null
        }
    }

    var refreshToken:String? = null
        get() {
            field = getString(SpKey.REFRESH_TOKEN)
            return field
        }set(value) {
        field = if (value != null){
            saveString(SpKey.REFRESH_TOKEN, value)
            value
        }else{
            clearData(SpKey.REFRESH_TOKEN)
            null
        }
    }
}