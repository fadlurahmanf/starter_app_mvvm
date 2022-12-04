package com.fadlurahmanf.starterappmvvm.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.starterappmvvm.constant.RoomKey
import com.fadlurahmanf.starterappmvvm.data.room.dao.SurahDao
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse

@Database(entities = [
    SurahResponse::class
], version = QuranDatabase.VERSION)
abstract class QuranDatabase:RoomDatabase() {
    abstract fun surahDao():SurahDao

    companion object{
        const val VERSION = 1
        @Volatile
        private var INSTANCE:QuranDatabase? = null
        fun getDatabase(context: Context):QuranDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuranDatabase::class.java,
                    RoomKey.quranDb
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}