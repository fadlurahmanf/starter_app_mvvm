package com.fadlurahmanf.starterappmvvm.core.room.domain.repository

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import com.fadlurahmanf.starterappmvvm.core.room.domain.common.ExampleDatabase
import javax.inject.Inject

class ExampleRoomDatasource @Inject constructor(
    context: Context
) {
    private var dao = ExampleDatabase.getDatabase(context).exampleDao()

    fun insertAll(list: List<ExampleRoomEntity>) = dao.insertAll(list)
    fun insert(value: ExampleRoomEntity) = dao.insert(value)
    fun get() = dao.getAll()
    fun delete() = dao.delete()
}