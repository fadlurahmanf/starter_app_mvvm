package com.fadlurahmanf.starterappmvvm.unknown.data.storage.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoRSA
import com.fadlurahmanf.starterappmvvm.feature.sp.domain.common.BasePreference
import javax.inject.Inject

class AuthSpStorage @Inject constructor(
    context: Context,
    crypto: CryptoRSA
) : BasePreference(context, crypto) {
    var accessToken: String? = null
        get() {
            field = getString(AppConstant.Sp.ACCESS_TOKEN)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveString(AppConstant.Sp.ACCESS_TOKEN, value)
                value
            } else {
                clearData(AppConstant.Sp.ACCESS_TOKEN)
                null
            }
        }

    var refreshToken: String? = null
        get() {
            field = getString(AppConstant.Sp.REFRESH_TOKEN)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveString(AppConstant.Sp.REFRESH_TOKEN, value)
                value
            } else {
                clearData(AppConstant.Sp.REFRESH_TOKEN)
                null
            }
        }
}