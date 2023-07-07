package com.fadlurahmanf.starterappmvvm.extension

import com.fadlurahmanf.starterappmvvm.core.base.NetworkState
import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException

fun String.toErrorState(): NetworkState<Nothing> {
    return NetworkState.Error(
        exception = CustomException(
            rawMessage = this
        )
    )
}