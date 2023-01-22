package com.fadlurahmanf.starterappmvvm.extension

import com.fadlurahmanf.starterappmvvm.base.NetworkState
import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException

fun Throwable.toErrorState():NetworkState<Nothing>{
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