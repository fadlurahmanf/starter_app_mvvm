package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.base.BaseViewModel
import com.fadlurahmanf.starterappmvvm.base.BaseViewState
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.data.entity.example.ExampleEntity
import com.fadlurahmanf.starterappmvvm.data.storage.example.ExampleSpStorage
import com.fadlurahmanf.starterappmvvm.data.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import com.fadlurahmanf.starterappmvvm.extension.uiSubscribe
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    var exampleEntity: ExampleEntity,
    var exampleSpStorage: ExampleSpStorage
):BaseViewModel() {

    private var stateData = BaseViewState<BaseResponse<List<TestimonialResponse>>>()

    private var _exampleState = MutableLiveData(stateData)
    val exampleState get() : LiveData<BaseViewState<BaseResponse<List<TestimonialResponse>>>> = _exampleState

    fun getTestimonial(){
        stateData = BaseViewState(
            state = STATE.LOADING
        )
        _exampleState.value = stateData
        addSubscription(exampleEntity.getTestimonial().uiSubscribe(
            {
                if (it.code == 100){
                    stateData = BaseViewState(
                        state = STATE.SUCCESS,
                        data = it
                    )
                }else{
                    stateData = BaseViewState(
                        state = STATE.FAILED,
                        error = it.message
                    )
                }
                _exampleState.value = stateData
            },
            {
                stateData = BaseViewState(
                    state = STATE.FAILED,
                    error = it.message
                )
                _exampleState.value = stateData
            },
            {}
        ))
    }
}