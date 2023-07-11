package com.fadlurahmanf.starterappmvvm.feature.logger.presentation.handler

import android.util.Log
import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationImpl
import com.fadlurahmanf.starterappmvvm.core.external.extension.formatDate5
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.interactor.LoggerInteractor
import java.util.Calendar

class LogConsole(
    private val loggerInteractor: LoggerInteractor,
    private val notificationImpl: NotificationImpl
) {

    private fun createDate() = Calendar.getInstance().time.formatDate5()

    private fun log(type: String, message: String, showNotification: Boolean) {
        loggerInteractor.insert(
            entity = LoggerEntity(
                type = type,
                message = message,
                date = createDate(),
            )
        )
        if (showNotification) {
            notificationImpl.showNotification(title = type, body = message)
        }
    }

    fun i(message: String, showNotification: Boolean = false) {
        Log.i("LOG CONSOLE INFO", message)
        log("INFO", message, showNotification)
    }

    fun d(message: String, showNotification: Boolean = false) {
        Log.i("LOG CONSOLE DEBUG", message)
        log("DEBUG", message, showNotification)
    }

    fun e(message: String, showNotification: Boolean = false) {
        Log.e("LOG CONSOLE ERROR", message)
        log("ERROR", message, showNotification)
    }

    fun w(message: String, showNotification: Boolean = false) {
        Log.w("LOG CONSOLE WARN", message)
        log("WARN", message, showNotification)
    }
}