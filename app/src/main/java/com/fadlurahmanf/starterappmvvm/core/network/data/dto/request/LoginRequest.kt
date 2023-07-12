package com.fadlurahmanf.starterappmvvm.core.network.data.dto.request


data class LoginRequest(
    var nik: String,
    var deviceId: String,
    var deviceToken: String,
    var password: String,
    var phoneNumber: String,
    var clientTimeMilis: String,
    var activationId:String
)
