package com.fadlurahmanf.starterappmvvm.data.repository.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.base.BasePreference
import javax.inject.Inject

class ExampleRepository @Inject constructor(
    var context: Context
) : BasePreference(context) {

    companion object{
        const val UUID_WORK = "UUID_WORK"
    }

    var uuidString: String ?= null
    get() {
        field = getString(UUID_WORK)
        return field
    }set(value) {
        if (value == null) {
            clearData(UUID_WORK)
            field = null
        }
        else {
            saveString(UUID_WORK, value)
            field = value
        }
    }
}