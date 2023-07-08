package com.fadlurahmanf.starterappmvvm.feature.logger.presentation

import com.fadlurahmanf.starterappmvvm.core.domain.usecase.NotificationImpl
import com.fadlurahmanf.starterappmvvm.core.external.extension.formatDate5
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.usecases.LoggerImpl
import java.util.Calendar

class LogConsole(
    private val loggerImpl: LoggerImpl,
    private val notificationImpl: NotificationImpl
) {

    private fun createDate() = Calendar.getInstance().time.formatDate5()

    private fun log(type: String, message: String, showNotification: Boolean) {
        loggerImpl.insert(
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
        log("INFO", message, showNotification)
    }

    fun d(message: String, showNotification: Boolean = false) {
        log("DEBUG", message, showNotification)
    }
}