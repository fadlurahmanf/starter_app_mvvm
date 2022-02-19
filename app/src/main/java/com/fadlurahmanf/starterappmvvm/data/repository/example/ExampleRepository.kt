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
        field = getData(UUID_WORK, null, String::class.java)
        return field
    }set(value) {
        if (value == null) clearData(UUID_WORK)
        else saveData(UUID_WORK, value)
        field = value
    }
}