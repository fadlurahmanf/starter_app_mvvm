package com.fadlurahmanf.starterappmvvm.data.storage.example

//import android.content.Context
//import com.fadlurahmanf.starterappmvvm.base.BasePreference
//import javax.inject.Inject
//
//class QuranStorageDatasource @Inject constructor(
//    var context: Context
//) : BasePreference(context) {
//
//    var uuidString: String ?= null
//    get() {
//        field = getString(UUID_WORK)
//        return field
//    }set(value) {
//        if (value == null) {
//            clearData(UUID_WORK)
//            field = null
//        }
//        else {
//            saveString(UUID_WORK, value)
//            field = value
//        }
//    }
//
//    var testimonialResponseCode:String?=null
//    get() {
//        field = getString(TESTIMONIAL_RESPONSE_CODE)
//        return field
//    }set(value) {
//        if (value==null){
//            clearData(TESTIMONIAL_RESPONSE_CODE)
//            field = null
//        }else{
//            saveString(TESTIMONIAL_RESPONSE_CODE, value)
//            field = value
//        }
//    }
//
//    var testimonialResponseMessage:String?=null
//        get() {
//            field = getString(TESTIMONIAL_RESPONSE_MESSAGE)
//            return field
//        }set(value) {
//        if (value==null){
//            clearData(TESTIMONIAL_RESPONSE_MESSAGE)
//            field = null
//        }else{
//            saveString(TESTIMONIAL_RESPONSE_MESSAGE, value)
//            field = value
//        }
//    }
//}