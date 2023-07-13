package com.fadlurahmanf.starterappmvvm.core.network.domain.interactor

import com.fadlurahmanf.starterappmvvm.core.network.domain.repository.QrisRemoteDatasource
import com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.request.InquiryQrisRequest
import javax.inject.Inject

class TransactionInteractor @Inject constructor(
    private val qrisRemoteDatasource: QrisRemoteDatasource,
) {
    fun inquiryQris(request: InquiryQrisRequest) = qrisRemoteDatasource.inquiryQris(request)

}