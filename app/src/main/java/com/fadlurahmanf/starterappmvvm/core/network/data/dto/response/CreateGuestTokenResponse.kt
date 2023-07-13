package com.fadlurahmanf.starterappmvvm.core.network.data.dto.response

data class CreateGuestTokenResponse(
    var accessToken: String? = null,
    var expiresIn: Int? = null
)
