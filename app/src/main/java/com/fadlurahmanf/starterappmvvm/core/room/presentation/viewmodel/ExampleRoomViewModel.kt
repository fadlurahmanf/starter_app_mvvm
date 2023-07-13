package com.fadlurahmanf.starterappmvvm.core.room.presentation.viewmodel

import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import com.fadlurahmanf.starterappmvvm.core.room.domain.interactor.ExampleRoomInteractor
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExampleRoomViewModel @Inject constructor(
    private val roomInteractor: ExampleRoomInteractor
) : BaseViewModel() {

    fun insert(value: ExampleRoomEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            roomInteractor.insert(value)
        }
    }

    fun getAll() {
        compositeDisposable().add(
            roomInteractor.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    list.forEach { element ->
                        logConsole.d("RES: $element")
                    }
                },
                    {
                        logConsole.e("ERROR GET EXAMPLE ROOM: ${it.message}")
                    })
        )
    }
}