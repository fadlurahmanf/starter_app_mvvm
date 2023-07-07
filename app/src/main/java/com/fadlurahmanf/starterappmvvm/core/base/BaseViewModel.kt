package com.fadlurahmanf.starterappmvvm.core.base

import androidx.lifecycle.ViewModel
import com.fadlurahmanf.starterappmvvm.dto.response.core.BaseResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable


abstract class BaseViewModel:ViewModel() {
    fun disposable() = CompositeDisposable()

    fun <T> isVerify(response: BaseResponse<T>) = response.code == "200"
            && response.message == "SUCCESS"
            && response.data != null

    fun <T> isCodeVerify(response: BaseResponse<T>) = response.code == "200"
            && response.data != null
}