package com.fadlurahmanf.starterappmvvm.core.base

import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException

sealed class NetworkState<out T: Any>{
    object Idle : NetworkState<Nothing>()
    object Loading : NetworkState<Nothing>()
    data class Success<out T: Any>(val data:T) : NetworkState<T>()
    data class Error(val exception:CustomException): NetworkState<Nothing>()
}