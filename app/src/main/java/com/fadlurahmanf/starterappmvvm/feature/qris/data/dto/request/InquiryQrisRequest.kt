package com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.request

import com.google.gson.annotations.SerializedName

data class InquiryQrisRequest(
    @SerializedName("senderAccountNumber")
    val accountNumber:String,
    val qris:String
)
