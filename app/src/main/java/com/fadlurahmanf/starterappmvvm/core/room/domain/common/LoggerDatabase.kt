package com.fadlurahmanf.starterappmvvm.core.room.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.converter.DateConverter
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repository.LoggerDao

@Database(
    entities = [
        LoggerEntity::class
    ], version = GeneralDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(value = [DateConverter::class])
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao

    companion object {
        const val VERSION = RoomConstant.DBVersion.logger

        @Volatile
        private var INSTANCE: LoggerDatabase? = null
        fun getDatabase(context: Context): LoggerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoggerDatabase::class.java,
                    AppConstant.RoomDB.logger
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}