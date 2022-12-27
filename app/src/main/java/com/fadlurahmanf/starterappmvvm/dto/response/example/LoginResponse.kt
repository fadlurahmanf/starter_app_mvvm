package com.fadlurahmanf.starterappmvvm.dto.response.example

data class LoginResponse(
    var type:String? = null,
    var accessToken:String? = null,
    var refreshToken:String? = null,
)
