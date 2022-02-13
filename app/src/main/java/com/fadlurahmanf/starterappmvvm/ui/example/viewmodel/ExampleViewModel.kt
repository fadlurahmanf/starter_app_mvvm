package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.base.BaseViewModel
import com.fadlurahmanf.starterappmvvm.data.entity.example.ExampleEntity
import com.fadlurahmanf.starterappmvvm.data.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import com.fadlurahmanf.starterappmvvm.extension.uiSubscribe
import javax.inject.Inject

class ExampleViewModel @Inject constructor(
    var exampleEntity: ExampleEntity
):BaseViewModel() {
    private var _testimonialLoading = MutableLiveData<Boolean>()
    var testimonialLoading = _testimonialLoading

    private var _testimonial = MutableLiveData<BaseResponse<List<TestimonialResponse>>>()
    var testimonial:LiveData<BaseResponse<List<TestimonialResponse>>> = _testimonial

    fun getTestimonial(){
        _testimonialLoading.postValue(true)
        addSubscription(exampleEntity.getTestimonial().uiSubscribe(
            {
                _testimonialLoading.postValue(false)
                if (it.code == "100"){
                    _testimonial.postValue(it)
                }else{
                    println("MASUK ERROR SUCCESS ${it.message}")
                }
            },
            {
                println("MASUK TESTIMONIAL ERROR ${it.message}")
                _testimonialLoading.postValue(false)
            },
            {}
        ))
    }
}