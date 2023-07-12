package com.fadlurahmanf.starterappmvvm.core.unknown.external.extension

import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.SdfConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.data.dto.exception.CustomException
import java.util.Date

fun String.toErrorState(): CustomState<Nothing> {
    return CustomState.Error(
        exception = CustomException(
            rawMessage = this
        )
    )
}

fun String.toDate1(): Date? {
    return try {
        val sdf = SdfConstant.sdf1
        sdf.parse(this)
    } catch (e: Throwable) {
        null
    }
}