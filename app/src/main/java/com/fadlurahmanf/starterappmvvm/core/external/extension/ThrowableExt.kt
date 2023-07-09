package com.fadlurahmanf.starterappmvvm.core.external.extension

import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.dto.exception.CustomException

fun Throwable.toErrorState(): CustomState<Nothing> {
    return if (this is CustomException){
        CustomState.Error(
            exception = this
        )
    }else{
        CustomState.Error(
            exception = CustomException(
                rawMessage = message
            )
        )
    }
}