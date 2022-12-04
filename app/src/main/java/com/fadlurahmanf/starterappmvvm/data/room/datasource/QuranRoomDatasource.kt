package com.fadlurahmanf.starterappmvvm.data.room.datasource

import android.content.Context
import com.fadlurahmanf.starterappmvvm.data.room.database.QuranDatabase
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse
import javax.inject.Inject

class QuranRoomDatasource @Inject constructor(
    context: Context
) {
    private var instance:QuranDatabase = QuranDatabase.getDatabase(context)

    fun insertAll(surahs:List<SurahResponse>) = instance.surahDao().insertAll(surahs)

    fun getAll() = instance.surahDao().getAll()
}