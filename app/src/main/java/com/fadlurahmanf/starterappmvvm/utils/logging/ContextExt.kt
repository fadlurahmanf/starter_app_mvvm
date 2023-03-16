package com.fadlurahmanf.starterappmvvm.utils.logging


import android.content.Context
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector

fun loge(message: String){
    Log.e("DEFAULT_TAG", message)
}

fun logd(message: String){
    Log.d("DEFAULT_TAG", message)
}

fun Context.cLoge(tag:String, message:String){
    ChuckerCollector(this).onError(
        tag,
        Throwable(message))
}

fun Context.cLoge(tag:String, message:Throwable){
    ChuckerCollector(this).onError(
        tag,
        message)
}

fun Context.cLoge(message:Throwable){
    ChuckerCollector(this).onError(
        "DEFAULT_TAG",
        message)
}

fun Context.cLogi(message:String, tag:String){
    ChuckerCollector(this).onError(
        tag,
        Throwable(message))
}
fun Context.cLogi(message:String){
    ChuckerCollector(this).onError(
        "INFO",
        Throwable(message))
}

fun Context.cLoge(message:String){
    ChuckerCollector(this).onError(
        "DEFAULT_TAG",
        Throwable(message))
}