package com.fadlurahmanf.starterappmvvm.feature.logger.presentation

import com.fadlurahmanf.starterappmvvm.feature.notification.domain.usecases.NotificationImpl
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.usecases.LoggerImpl
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.refEq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class LogConsoleTest {
    lateinit var loggerImpl: LoggerImpl
    lateinit var notificationImpl: NotificationImpl
    lateinit var logConsole: LogConsole

    @Before
    fun setupBefore() {
        loggerImpl = mock<LoggerImpl>()
        notificationImpl = mock<NotificationImpl>()
        logConsole = LogConsole(loggerImpl, notificationImpl)
    }

    @Test
    fun `insert type info when use LogConsole i`() {
        logConsole.i("TES")
        verify(loggerImpl, times(1)).insert(
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