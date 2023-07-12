package com.fadlurahmanf.starterappmvvm.core.network.data.dto.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("token")
    var refreshToken: String
)
