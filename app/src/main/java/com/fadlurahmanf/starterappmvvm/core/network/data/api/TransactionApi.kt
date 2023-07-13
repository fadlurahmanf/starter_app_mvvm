package com.fadlurahmanf.starterappmvvm.core.network.data.api

import com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.request.InquiryQrisRequest
import com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.response.InquiryQrisResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface TransactionApi {

    @POST("qris/inquiry")
    fun inquiryQris(
        @Body body: InquiryQrisRequest
    ): Observable<BaseResponse<InquiryQrisResponse>>
}