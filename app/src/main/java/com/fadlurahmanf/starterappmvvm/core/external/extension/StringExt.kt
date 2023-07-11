package com.fadlurahmanf.starterappmvvm.core.external.extension

import com.fadlurahmanf.starterappmvvm.core.data.constant.SdfConstant
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.data.dto.exception.CustomException
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