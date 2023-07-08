package com.fadlurahmanf.starterappmvvm.feature.logger.presentation

import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.usecases.LoggerImpl
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LogConsoleTest {
    @InjectMocks
    lateinit var logConsole: LogConsole

    @Mock
    lateinit var loggerImpl: LoggerImpl

    @Before
    fun setupBefore() {
        println("SETUP BEFORE")
        MockitoAnnotations.openMocks(this)
        logConsole = LogConsole(loggerImpl)
    }

    fun createRandomLoggerEntity():LoggerEntity{
        return LoggerEntity()
    }

    @Test
    fun `insert type info when use LogConsole i`() {

        logConsole.i("TES")

        Mockito.verify(loggerImpl, Mockito.times(1)).insert(LoggerEntity(
            message = "TES",
            date = Mockito.any(),
            type = "INFO",
            writer = Mockito.any()
        ))
    }

    @Test
    fun `test_2`() {
        println("TES 2")
        assertEquals(0, 0)
    }
}