package com.fadlurahmanf.starterappmvvm.core.data

import com.fadlurahmanf.starterappmvvm.unknown.dto.exception.CustomException

sealed class CustomState<out T: Any>{
    object Idle : CustomState<Nothing>()
    object Loading : CustomState<Nothing>()
    data class Success<out T: Any>(val data:T) : CustomState<T>()
    data class Error(val exception: CustomException): CustomState<Nothing>()
}