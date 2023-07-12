package com.fadlurahmanf.starterappmvvm.core.room.domain.interactor

import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import com.fadlurahmanf.starterappmvvm.core.room.domain.repository.ExampleRoomDatasource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ExampleRoomInteractor @Inject constructor(
    private val roomDatasource: ExampleRoomDatasource
) {
    fun insert(value: ExampleRoomEntity) {
        return roomDatasource.insert(value)
    }

    fun getAll(): Single<List<ExampleRoomEntity>> {
        return roomDatasource.get()
    }
}