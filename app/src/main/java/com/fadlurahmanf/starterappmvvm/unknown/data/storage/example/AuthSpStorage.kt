package com.fadlurahmanf.starterappmvvm.unknown.data.storage.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoRSA
import com.fadlurahmanf.starterappmvvm.core.sp.data.constant.SpConstant
import com.fadlurahmanf.starterappmvvm.core.sp.domain.common.BasePreference
import javax.inject.Inject

class AuthSpStorage @Inject constructor(
    context: Context,
    cryptoAES: CryptoAES
) : BasePreference(context, cryptoAES) {
    var accessToken: String? = null
        get() {
            field = getString(SpConstant.ACCESS_TOKEN)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveString(SpConstant.ACCESS_TOKEN, value)
                value
            } else {
                clearData(SpConstant.ACCESS_TOKEN)
                null
            }
        }

    var refreshToken: String? = null
        get() {
            field = getString(SpConstant.REFRESH_TOKEN)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveString(SpConstant.REFRESH_TOKEN, value)
                value
            } else {
                clearData(SpConstant.REFRESH_TOKEN)
                null
            }
        }

    var guestToken: String? = null
        get() {
            field = getString(SpConstant.GUEST_TOKEN)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveString(SpConstant.GUEST_TOKEN, value)
                value
            } else {
                clearData(SpConstant.GUEST_TOKEN)
                null
            }
        }
}