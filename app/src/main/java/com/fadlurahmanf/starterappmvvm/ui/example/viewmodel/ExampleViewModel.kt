package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.base.BaseViewModel
import com.fadlurahmanf.starterappmvvm.base.BaseViewState
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.data.entity.example.ExampleEntity
import com.fadlurahmanf.starterappmvvm.data.repository.example.ExampleRepository
import com.fadlurahmanf.starterappmvvm.data.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import com.fadlurahmanf.starterappmvvm.extension.uiSubscribe
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    var exampleEntity: ExampleEntity,
    var exampleRepository: ExampleRepository
):BaseViewModel() {
    private var exampleViewState = BaseViewState<BaseResponse<List<TestimonialResponse>>>()

    private var _testimonialError = MutableLiveData<String?>()
    var testimonialError = _testimonialError

    private var _testimonialLoading = MutableLiveData<Boolean>()
    var testimonialLoading = _testimonialLoading

    private var _testimonial = MutableLiveData<BaseResponse<List<TestimonialResponse>>>()
    var testimonial:LiveData<BaseResponse<List<TestimonialResponse>>> = _testimonial

    private var _exampleState = MutableLiveData<BaseViewState<BaseResponse<List<TestimonialResponse>>>>()
    var exampleState = _exampleState

    fun getTestimonial(){
        _testimonialLoading.postValue(true)
        addSubscription(exampleEntity.getTestimonial().uiSubscribe(
            {
                _testimonialLoading.postValue(false)
                if (it.code == 100){
                    _testimonial.postValue(it)
                }else{
                    _testimonialError.postValue(it.message)
                }
            },
            {
                _testimonialLoading.postValue(false)
                _testimonialError.postValue(it.message)
            },
            {}
        ))
    }

    fun getTestimonialState(){
        exampleViewState.state = STATE.LOADING
        _exampleState.postValue(exampleViewState)
        addSubscription(exampleEntity.getTestimonial().uiSubscribe(
            {
                if (it.code == 100){
                    exampleViewState.state = STATE.SUCCESS
                    exampleViewState.data = it
                    _exampleState.postValue(exampleViewState)
                }else{
                    exampleViewState.state = STATE.FAILED
                    exampleViewState.error = it.message
                    _exampleState.postValue(exampleViewState)
                }
            },
            {
                exampleViewState.error = it.message
                _exampleState.postValue(exampleViewState)
            },
            {}
        ))
    }
}