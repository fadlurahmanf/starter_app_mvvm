package com.fadlurahmanf.starterappmvvm.base

import androidx.lifecycle.ViewModel
import com.fadlurahmanf.starterappmvvm.dto.response.core.BaseResponse
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable


abstract class BaseViewModel:ViewModel() {
    fun disposable() = CompositeDisposable()

    fun <T> isVerify(response: BaseResponse<T>) = response.code == "200"
            && response.message == "SUCCESS"
            && response.data != null

    fun <T> isCodeVerify(response: BaseResponse<T>) = response.code == "200"
            && response.data != null
}