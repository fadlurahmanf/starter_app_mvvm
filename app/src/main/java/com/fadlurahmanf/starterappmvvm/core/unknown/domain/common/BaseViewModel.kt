package com.fadlurahmanf.starterappmvvm.core.unknown.domain.common

import androidx.lifecycle.ViewModel
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {
    fun disposable() = CompositeDisposable()

    fun <T> isVerify(response: BaseResponse<T>) = response.code == "200"
            && response.message == "SUCCESS"
            && response.data != null

    fun <T> isCodeVerify(response: BaseResponse<T>) = response.code == "200"
            && response.data != null
}