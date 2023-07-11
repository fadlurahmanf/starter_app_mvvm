package com.fadlurahmanf.starterappmvvm.core.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.starterappmvvm.core.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repository.LoggerDao
import com.fadlurahmanf.starterappmvvm.unknown.data.room.dao.SurahDao
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse

@Database(
    entities = [
        SurahResponse::class,
    ], version = GeneralDatabase.VERSION,
    exportSchema = false
)
abstract class GeneralDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao

    companion object {
        const val VERSION = 1

        @Volatile
        private var INSTANCE: GeneralDatabase? = null
        fun getDatabase(context: Context): GeneralDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeneralDatabase::class.java,
                    AppConstant.RoomDB.core
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Database(
    entities = [
        LoggerEntity::class
    ], version = GeneralDatabase.VERSION,
    exportSchema = false
)
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao

    companion object {
        const val VERSION = 1

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