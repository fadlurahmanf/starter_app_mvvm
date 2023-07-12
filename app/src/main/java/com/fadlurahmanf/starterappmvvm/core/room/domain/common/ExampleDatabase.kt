package com.fadlurahmanf.starterappmvvm.core.room.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.room.data.dao.ExampleDao
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import com.fadlurahmanf.starterappmvvm.core.room.external.AddOnConverter

@Database(
    entities = [
        ExampleRoomEntity::class
    ], version = ExampleDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(value = [AddOnConverter::class])
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

