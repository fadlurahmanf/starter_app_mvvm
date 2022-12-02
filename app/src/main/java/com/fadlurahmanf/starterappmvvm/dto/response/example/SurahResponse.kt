package com.fadlurahmanf.starterappmvvm.dto.response.example

import com.google.gson.annotations.SerializedName

data class SurahResponse(
    @SerializedName("number")
    var number:Int?=null,
    @SerializedName("name")
    var name:String?=null,
)
