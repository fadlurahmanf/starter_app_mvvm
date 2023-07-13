package com.fadlurahmanf.starterappmvvm.feature.qris.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.AuthenticationInteractor
import com.fadlurahmanf.starterappmvvm.core.network.domain.interactor.TransactionInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseAfterLoginViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toErrorState
import com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.request.InquiryQrisRequest
import com.fadlurahmanf.starterappmvvm.feature.qris.data.dto.response.InquiryQrisResponse
import com.fadlurahmanf.starterappmvvm.unknown.data.storage.example.AuthSpStorage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class QrisViewModel @Inject constructor(
    authenticationInteractor: AuthenticationInteractor,
    authSpStorage: AuthSpStorage,
    private val transactionInteractor: TransactionInteractor
) : BaseAfterLoginViewModel(authenticationInteractor, authSpStorage) {

    private val _inquiryState = MutableLiveData<CustomState<InquiryQrisResponse>>()
    val inquiryState get():LiveData<CustomState<InquiryQrisResponse>> = _inquiryState

    fun inquiryQris(qrisRaw: String) {
        val request = InquiryQrisRequest(
            accountNumber = "1001931098",
            qris = qrisRaw
        )
        _inquiryState.value = CustomState.Idle
        _inquiryState.value = CustomState.Loading
        compositeDisposable().add(
            transactionInteractor.inquiryQris(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    logConsole.d("SUCCESS INQUIRY QRIS ${it.data}")
                    if (it.data != null){
                        _inquiryState.value = CustomState.Success(data = it.data!!)
                    }else{
                        _inquiryState.value = it.message?.toErrorState()
                    }
                }, {
                    logConsole.e("ERROR INQUIRY QRIS ${it.message}")
                    _inquiryState.value = it.toErrorState()
                }, {})
        )
    }
}