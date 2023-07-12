package com.fadlurahmanf.starterappmvvm.feature.logger.domain.interactor

import android.util.Log
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
        } catch (e: Throwable) {
            Log.e("LOG CONSOLE", "ERROR INSERT LOG ${e.message}")
        }
    }

    // get all in text
    fun getAll(): Observable<List<LoggerEntity>> {
        try {
            return loggerRoomDatasource.getAll().toObservable()
        } catch (e: Throwable) {
            throw e
        }
    }

    fun deleteLogs() {
        try {
            return loggerRoomDatasource.deleteAll()
        } catch (e: Throwable) {
            throw e
        }
    }
}