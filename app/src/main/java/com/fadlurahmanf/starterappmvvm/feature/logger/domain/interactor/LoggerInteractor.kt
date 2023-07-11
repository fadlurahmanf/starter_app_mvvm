package com.fadlurahmanf.starterappmvvm.feature.logger.domain.interactor

import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repository.LoggerRoomDatasource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LoggerInteractor @Inject constructor(
    private var loggerRoomDatasource: LoggerRoomDatasource
) {
    fun insert(entity: LoggerEntity) {
        try {
            loggerRoomDatasource.insert(entity)
        } catch (_: Throwable) {
        }
    }

    // get all in text
    fun getAll(): Observable<List<String>> {
        try {
            return loggerRoomDatasource.getAll().toObservable()
                .map { list ->
                    list.map { entity ->
                        "[${entity.date}][${entity.type}]: ${entity.message}"
                    }
                }
        } catch (e: Throwable) {
            throw e
        }
    }
}