package com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.response

import com.google.gson.annotations.SerializedName

data class InquiryQrisResponse(
    val acquireName: String? = null,
    var merchantName: String? = null,
    var merchantCity: String? = null,
    var tipIndicator: String? = null,
    var tip: String? = null,
    var tipPercentage: String? = null,
    var amount: String? = null,
    var total: String? = null,
)
