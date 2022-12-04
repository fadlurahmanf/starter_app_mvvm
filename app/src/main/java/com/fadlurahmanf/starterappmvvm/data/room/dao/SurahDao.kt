package com.fadlurahmanf.starterappmvvm.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fadlurahmanf.starterappmvvm.constant.RoomKey
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse
import io.reactivex.rxjava3.core.Single

@Dao
interface SurahDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(surahs:List<SurahResponse>)

    @Query("SELECT * FROM ${RoomKey.surahTable}")
    fun getAll(): Single<List<SurahResponse>>

    @Query("DELETE FROM ${RoomKey.surahTable}")
    fun remove()
}