package com.fadlurahmanf.starterappmvvm.feature.logger.domain.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppKey
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface LoggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(loggers:List<LoggerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logger:LoggerEntity)

    @Query("SELECT * FROM ${AppKey.RoomTable.logger}")
    fun getAll(): Single<List<LoggerEntity>>

    @Query("DELETE FROM ${AppKey.RoomTable.logger}")
    fun remove()
}