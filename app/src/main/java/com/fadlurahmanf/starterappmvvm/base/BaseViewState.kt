package com.fadlurahmanf.starterappmvvm.base

import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException

data class BaseViewState<T> (
    var data:T ?= null,
    var error:String ?= null,
    var state:STATE ?= null
)

enum class STATE{
    IDLE, LOADING, SUCCESS, FAILED
}

sealed class NetworkState<out T: Any>{
    object Idle : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
    data class Success<out T: Any>(val data:T) : NetworkState<T>()
    data class Error(val exception:CustomException): NetworkState<Nothing>()
}