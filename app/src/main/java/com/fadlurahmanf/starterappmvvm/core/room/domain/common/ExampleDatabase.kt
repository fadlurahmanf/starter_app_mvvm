package com.fadlurahmanf.starterappmvvm.core.room.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.room.data.dao.ExampleDao
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity

@Database(
    entities = [
        ExampleRoomEntity::class
    ], version = ExampleDatabase.VERSION,
    exportSchema = false
)
abstract class ExampleDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao

    companion object {
        const val VERSION = RoomConstant.DBVersion.example
        private var INSTANCE: ExampleDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): ExampleDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ExampleDatabase::class.java,
                    RoomConstant.DB.example
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!
        }
    }
}

