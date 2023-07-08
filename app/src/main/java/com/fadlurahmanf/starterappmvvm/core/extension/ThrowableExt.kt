package com.fadlurahmanf.starterappmvvm.core.extension

import com.fadlurahmanf.starterappmvvm.core.data.NetworkState
import com.fadlurahmanf.starterappmvvm.unknown.dto.exception.CustomException

fun Throwable.toErrorState(): NetworkState<Nothing> {
    return if (this is CustomException){
        NetworkState.Error(
            exception = this
        )
    }else{
        NetworkState.Error(
            exception = CustomException(
                rawMessage = message
            )
        )
    }
}