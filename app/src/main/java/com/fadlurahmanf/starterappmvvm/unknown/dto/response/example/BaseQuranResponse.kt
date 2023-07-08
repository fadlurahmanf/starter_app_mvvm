package com.fadlurahmanf.starterappmvvm.unknown.dto.response.example

import com.google.gson.annotations.SerializedName

data class BaseQuranResponse<T>(
    @SerializedName("code")
    var code:Int? = null,
    @SerializedName("status")
    var status:String? = null,
    @SerializedName("data")
    var data:T ?= null
)