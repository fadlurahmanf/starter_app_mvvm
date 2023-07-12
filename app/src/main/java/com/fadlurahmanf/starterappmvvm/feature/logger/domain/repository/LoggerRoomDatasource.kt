package com.fadlurahmanf.starterappmvvm.feature.logger.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.room.domain.common.LoggerDatabase
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import javax.inject.Inject

class LoggerRoomDatasource @Inject constructor(
    context: Context
) {
    private var instance: LoggerDatabase = LoggerDatabase.getDatabase(context)

    fun insertAll(loggers: List<LoggerEntity>) = instance.loggerDao().insertAll(loggers)
    fun insert(logger: LoggerEntity) = instance.loggerDao().insert(logger)
    fun getAll() = instance.loggerDao().getAll()
    fun deleteAll() = instance.loggerDao().delete()
}