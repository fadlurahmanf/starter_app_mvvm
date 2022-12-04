package com.fadlurahmanf.starterappmvvm.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BaseViewModel:ViewModel() {
    fun disposable() = CompositeDisposable()
}