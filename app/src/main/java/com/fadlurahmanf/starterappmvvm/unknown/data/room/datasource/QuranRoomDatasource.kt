package com.fadlurahmanf.starterappmvvm.unknown.data.room.datasource

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.domain.common.QuranDatabase
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse
import javax.inject.Inject

class QuranRoomDatasource @Inject constructor(
    context: Context
) {
    private var instance: QuranDatabase = QuranDatabase.getDatabase(context)

    fun insertAll(surahs:List<SurahResponse>) = instance.surahDao().insertAll(surahs)

    fun getAll() = instance.surahDao().getAll()
}