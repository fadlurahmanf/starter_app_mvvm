package com.fadlurahmanf.starterappmvvm.feature.logger.domain.usecases

import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repositories.LoggerRoomDatasource
import javax.inject.Inject

class LoggerImpl @Inject constructor(
    private var loggerRoomDatasource: LoggerRoomDatasource
) {
    fun insert(entity: LoggerEntity) {
        try {
            loggerRoomDatasource.insert(entity)
        } catch (_: Throwable) {
        }
    }
}