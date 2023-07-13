package com.fadlurahmanf.starterappmvvm.core.network.data.dto.response

data class LoginResponse(
    var type:String? = null,
    var accessToken:String? = null,
    var refreshToken:String? = null,
)
