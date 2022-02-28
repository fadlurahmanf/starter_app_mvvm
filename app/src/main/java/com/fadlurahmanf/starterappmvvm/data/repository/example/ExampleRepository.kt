package com.fadlurahmanf.starterappmvvm.data.repository.example

import android.content.Context
import com.fadlurahmanf.starterappmvvm.base.BasePreference
import com.fadlurahmanf.starterappmvvm.data.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import javax.inject.Inject

class ExampleRepository @Inject constructor(
    var context: Context
) : BasePreference(context) {

    companion object{
        const val UUID_WORK = "UUID_WORK"
        const val TESTIMONIAL_RESPONSE_CODE = "TESTIMONIAL_RESPONSE_CODE"
        const val TESTIMONIAL_RESPONSE_MESSAGE = "TESTIMONIAL_RESPONSE_MESSAGE"
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

    var testimonialResponseCode:String?=null
    get() {
        field = getString(TESTIMONIAL_RESPONSE_CODE)
        return field
    }set(value) {
        if (value==null){
            clearData(TESTIMONIAL_RESPONSE_CODE)
            field = null
        }else{
            saveString(TESTIMONIAL_RESPONSE_CODE, value)
            field = value
        }
    }

    var testimonialResponseMessage:String?=null
        get() {
            field = getString(TESTIMONIAL_RESPONSE_MESSAGE)
            return field
        }set(value) {
        if (value==null){
            clearData(TESTIMONIAL_RESPONSE_MESSAGE)
            field = null
        }else{
            saveString(TESTIMONIAL_RESPONSE_MESSAGE, value)
            field = value
        }
    }
}