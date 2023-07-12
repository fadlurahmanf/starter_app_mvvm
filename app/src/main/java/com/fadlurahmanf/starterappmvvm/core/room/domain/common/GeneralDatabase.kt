package com.fadlurahmanf.starterappmvvm.core.room.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.unknown.data.room.dao.SurahDao
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse

@Database(
    entities = [
        SurahResponse::class,
    ], version = GeneralDatabase.VERSION,
    exportSchema = false
)
@Deprecated("DEPRECATED")
abstract class GeneralDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao

    companion object {
        const val VERSION = RoomConstant.DBVersion.general

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

