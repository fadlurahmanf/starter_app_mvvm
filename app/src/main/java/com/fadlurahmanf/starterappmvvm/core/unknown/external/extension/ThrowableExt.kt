package com.fadlurahmanf.starterappmvvm.core.unknown.external.extension

import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.exception.CustomException

fun Throwable.toErrorState(): CustomState<Nothing> {
    return if (this is CustomException) {
        CustomState.Error(
            exception = this
        )
    } else {
        CustomState.Error(
            exception = CustomException(
                rawMessage = message
            )
        )
    }
}