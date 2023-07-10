package com.fadlurahmanf.starterappmvvm.feature.logger.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.domain.common.GeneralDatabase
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import javax.inject.Inject

class LoggerRoomDatasource @Inject constructor(
    context: Context
) {
    private var instance: GeneralDatabase = GeneralDatabase.getDatabase(context)

    fun insertAll(loggers: List<LoggerEntity>) = instance.loggerDao().insertAll(loggers)
    fun insert(logger: LoggerEntity) = instance.loggerDao().insert(logger)
    fun getAll() = instance.loggerDao().getAll()
}