package com.fadlurahmanf.starterappmvvm.core.sp.data.storage

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.encrypt.presentation.CryptoAES
import com.fadlurahmanf.starterappmvvm.core.sp.data.constant.SpConstant
import com.fadlurahmanf.starterappmvvm.core.sp.data.dto.model.ExampleModelSp
import com.fadlurahmanf.starterappmvvm.core.sp.domain.common.BasePreference
import javax.inject.Inject

class ExampleSpStorage @Inject constructor(
    context: Context,
    cryptoAES: CryptoAES,
) : BasePreference(context, cryptoAES) {

    var exampleRawString: String? = null
        get() {
            field = getRawString(SpConstant.EXAMPLE_RAW_STRING)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveRawString(SpConstant.EXAMPLE_RAW_STRING, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_RAW_STRING)
                null
            }
        }

    var exampleString: String? = null
        get() {
            field = getString(SpConstant.EXAMPLE_STRING)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveString(SpConstant.EXAMPLE_STRING, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_STRING)
                null
            }
        }

    fun getRawExampleString(): String? = getRawString(SpConstant.EXAMPLE_STRING)

    var exampleInt: Int? = null
        get() {
            field = getInt(SpConstant.EXAMPLE_INT)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveInt(SpConstant.EXAMPLE_INT, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_INT)
                null
            }
        }

    fun getRawExampleInt(): String? = getRawString(SpConstant.EXAMPLE_INT)

    var exampleLong: Long? = null
        get() {
            field = getLong(SpConstant.EXAMPLE_LONG)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveLong(SpConstant.EXAMPLE_LONG, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_LONG)
                null
            }
        }

    fun getRawExampleLong(): String? = getRawString(SpConstant.EXAMPLE_LONG)

    var exampleFloat: Float? = null
        get() {
            field = getFloat(SpConstant.EXAMPLE_FLOAT)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveFloat(SpConstant.EXAMPLE_FLOAT, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_FLOAT)
                null
            }
        }

    fun getRawExampleFloat(): String? = getRawString(SpConstant.EXAMPLE_FLOAT)

    var exampleData: ExampleModelSp? = null
        get() {
            field = getData(SpConstant.EXAMPLE_DATA, ExampleModelSp::class.java)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveData(SpConstant.EXAMPLE_DATA, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_DATA)
                null
            }
        }

    fun getRawExampleData(): String? = getRawString(SpConstant.EXAMPLE_DATA)

    var exampleListData: ArrayList<ExampleModelSp>? = null
        get() {
            field = getListData(SpConstant.EXAMPLE_LIST_DATA, ExampleModelSp::class.java)
            return field
        }
        set(value) {
            field = if (value != null) {
                saveListData(SpConstant.EXAMPLE_LIST_DATA, value)
                value
            } else {
                clearData(SpConstant.EXAMPLE_LIST_DATA)
                null
            }
        }

    fun getRawExampleListData(): String? = getRawString(SpConstant.EXAMPLE_LIST_DATA)
}