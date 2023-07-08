package com.fadlurahmanf.starterappmvvm.core.external.extension

import com.fadlurahmanf.starterappmvvm.core.data.NetworkState
import com.fadlurahmanf.starterappmvvm.unknown.dto.exception.CustomException

fun String.toErrorState(): NetworkState<Nothing> {
    return NetworkState.Error(
        exception = CustomException(
            rawMessage = this
        )
    )
}