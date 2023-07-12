package com.fadlurahmanf.starterappmvvm.feature.logger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface LoggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(loggers:List<LoggerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logger:LoggerEntity)

    @Query("SELECT * FROM ${RoomConstant.Table.logger} ORDER BY date DESC")
    fun getAll(): Single<List<LoggerEntity>>

    @Query("DELETE FROM ${RoomConstant.Table.logger}")
    fun delete()
}