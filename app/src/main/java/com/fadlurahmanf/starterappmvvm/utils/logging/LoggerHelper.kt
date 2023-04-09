package com.fadlurahmanf.starterappmvvm.utils.logging


import android.content.Context
import android.os.Environment
import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.fadlurahmanf.starterappmvvm.BuildConfig
import java.io.File
import java.io.FileWriter
import java.util.Calendar

fun loge(message: String) {
    Log.e("DEFAULT_TAG", message)
    if (isLoggerFileExist() && isDevFlavor()) {
        appendTextToLogger(loggerFile(), "DEBUG ERROR", message)
    }
}

fun logd(message: String) {
    Log.d("DEFAULT_TAG", message)
    if (isLoggerFileExist() && isDevFlavor()) {
        appendTextToLogger(loggerFile(), "DEBUG INFO", message)
    }
}

fun loge(tag: String, message: String) {
    Log.e("DEFAULT_TAG", message)
    if (isLoggerFileExist() && isDevFlavor()) {
        appendTextToLogger(loggerFile(), tag, message)
    }
}

fun logd(tag: String, message: String) {
    Log.d("DEFAULT_TAG", message)
    if (isLoggerFileExist() && isDevFlavor()) {
        appendTextToLogger(loggerFile(), tag, message)
    }
}

private fun appendTextToLogger(file: File, tag: String, message: String) {
    val writer = FileWriter(file, true)
    writer.write("\n\n${Calendar.getInstance().time}: $tag -> $message")
    writer.close()
}

private fun appendTextToLogger(file: File, message: String) {
    val writer = FileWriter(file, true)
    writer.write("\n\n${Calendar.getInstance().time}: INFO -> $message")
    writer.close()
}

private fun newTextToLogger(file: File) {
    val writer = FileWriter(file, false)
    writer.write("===== LOGGER FILE =====")
    writer.close()
}

private fun loggerFile(): File {
    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    return File(downloadDir.path + "/starter_app_logger.txt")
}

private fun isLoggerFileExist(): Boolean {
    return loggerFile().isFile && loggerFile().exists()
}

@Suppress("KotlinConstantConditions")
private fun isDevFlavor(): Boolean = BuildConfig.FLAVOR == "dev"

fun createNewLoggerFile() {
    if (isDevFlavor()) {
        val loggerFile = loggerFile()
        if (!loggerFile.exists()) {
            loggerFile.createNewFile()
            Log.d("DEFAULT_TAG", "CREATED NEW LOGGER FILE ${loggerFile.path}")
            newTextToLogger(loggerFile())
        } else {
            Log.d("DEFAULT_TAG", "ALREADY EXIST")
            newTextToLogger(loggerFile())
        }
    } else {
        Log.d("DEFAULT_TAG", "NO NEED TO CREATE LOGGER FILE, FLAVOR IS ${BuildConfig.FLAVOR}")
    }
}


fun Context.cLoge(tag: String, message: String) {
    ChuckerCollector(this).onError(
        tag,
        Throwable(message)
    )
}

fun Context.cLoge(tag: String, message: Throwable) {
    ChuckerCollector(this).onError(
        tag,
        message
    )
}

fun Context.cLoge(message: Throwable) {
    ChuckerCollector(this).onError(
        "DEFAULT_TAG",
        message
    )
}

fun Context.cLogi(message: String, tag: String) {
    ChuckerCollector(this).onError(
        tag,
        Throwable(message)
    )
}

fun Context.cLogi(message: String) {
    ChuckerCollector(this).onError(
        "INFO",
        Throwable(message)
    )
}

fun Context.cLoge(message: String) {
    ChuckerCollector(this).onError(
        "DEFAULT_TAG",
        Throwable(message)
    )
}