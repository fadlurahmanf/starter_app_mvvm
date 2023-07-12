package com.fadlurahmanf.starterappmvvm.core.room.domain.interactor

import android.content.Context
import com.fadlurahmanf.starterappmvvm.core.room.data.dao.ExampleDao
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import com.fadlurahmanf.starterappmvvm.core.room.domain.repository.ExampleRoomDatasource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
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