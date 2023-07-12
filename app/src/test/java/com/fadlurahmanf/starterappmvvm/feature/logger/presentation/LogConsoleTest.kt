package com.fadlurahmanf.starterappmvvm.feature.logger.presentation

import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationImpl
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.core.logger.domain.interactor.LoggerInteractor
import com.fadlurahmanf.starterappmvvm.core.logger.presentation.handler.LogConsole
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class LogConsoleTest {
    lateinit var loggerInteractor: LoggerInteractor
    lateinit var notificationImpl: NotificationImpl
    lateinit var logConsole: LogConsole

    @Before
    fun setupBefore() {
        loggerInteractor = mock<LoggerInteractor>()
        notificationImpl = mock<NotificationImpl>()
        logConsole = LogConsole(loggerInteractor, notificationImpl)
    }

    @Test
    fun `insert type info when use LogConsole i`() {
        logConsole.i("TES")
        verify(loggerInteractor, times(1)).insert(
            refEq(
                LoggerEntity(
                    message = "TES",
                    type = "INFO"
                ), "date"
            )
        )
    }

    @Test
    fun `show notification when logging`() {
        logConsole.i("TES", showNotification = true)
        verify(notificationImpl, times(1)).showNotification(
            id = AppConstant.Notification.DEFAULT_NOTIFICATION_ID,
            title = "INFO",
            body = "TES"
        )

        logConsole.i("TES", showNotification = false)
        verify(notificationImpl, times(0)).showNotification(
            id = AppConstant.Notification.DEFAULT_NOTIFICATION_ID,
            title = "INFO",
            body = "TES"
        )
    }
}