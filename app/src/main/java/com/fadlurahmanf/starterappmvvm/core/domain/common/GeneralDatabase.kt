package com.fadlurahmanf.starterappmvvm.core.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.starterappmvvm.core.external.constant.AppKey
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.domain.repositories.LoggerDao
import com.fadlurahmanf.starterappmvvm.unknown.data.room.dao.SurahDao
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse

@Database(
    entities = [
        SurahResponse::class,
        LoggerEntity::class
    ], version = GeneralDatabase.VERSION,
    exportSchema = false
)
abstract class GeneralDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun loggerDao(): LoggerDao

    companion object {
        const val VERSION = 4

        @Volatile
        private var INSTANCE: GeneralDatabase? = null
        fun getDatabase(context: Context): GeneralDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeneralDatabase::class.java,
                    AppKey.RoomDB.core
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}