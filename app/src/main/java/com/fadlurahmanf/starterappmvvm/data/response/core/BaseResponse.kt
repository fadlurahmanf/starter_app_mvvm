package com.fadlurahmanf.starterappmvvm.data.response.core

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("code")
    var code:Int ?= null,
    @SerializedName("message")
    var message:String ?= null,
    @SerializedName("data")
    var data: T ?= null
)
