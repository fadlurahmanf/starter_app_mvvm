package com.fadlurahmanf.starterappmvvm.core.logger.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.external.extension.toErrorState
import com.fadlurahmanf.starterappmvvm.core.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.core.logger.domain.interactor.LoggerInteractor
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoggerViewModel @Inject constructor(
    private val loggerInteractor: LoggerInteractor
) : BaseViewModel() {

    private val _state = MutableLiveData<CustomState<List<LoggerEntity>>>()
    val state: LiveData<CustomState<List<LoggerEntity>>> = _state
    fun getAllHistory() {
        _state.value = CustomState.Idle
        _state.value = CustomState.Loading

        disposable().add(
            loggerInteractor.getAll().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    _state.value = CustomState.Success(data = it)
                }, {
                    _state.value = it.toErrorState()
                }, {})
        )
    }

    fun dispose() {
        disposable().dispose()
    }

    fun deleteLogs() {
        CoroutineScope(Dispatchers.IO).launch {
            loggerInteractor.deleteLogs()
        }
    }
}