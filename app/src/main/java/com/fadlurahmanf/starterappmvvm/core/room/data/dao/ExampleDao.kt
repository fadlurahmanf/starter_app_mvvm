package com.fadlurahmanf.starterappmvvm.core.room.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadlurahmanf.starterappmvvm.core.room.data.constant.RoomConstant
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface ExampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list:List<ExampleRoomEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: ExampleRoomEntity)

    @Query("SELECT * FROM ${RoomConstant.Table.example}")
    fun getAll(): Single<List<ExampleRoomEntity>>

    @Query("DELETE FROM ${RoomConstant.Table.example}")
    fun delete()
}