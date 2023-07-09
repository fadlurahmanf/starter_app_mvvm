package com.fadlurahmanf.starterappmvvm.core.external.extension

import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.dto.exception.CustomException

fun String.toErrorState(): CustomState<Nothing> {
    return CustomState.Error(
        exception = CustomException(
            rawMessage = this
        )
    )
}