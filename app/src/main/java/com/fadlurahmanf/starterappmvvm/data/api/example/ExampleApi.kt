package com.fadlurahmanf.starterappmvvm.data.api.example

import com.fadlurahmanf.starterappmvvm.data.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ExampleApi {
    @GET("api/testimonial/all")
    fun getTestimonial(): Observable<BaseResponse<List<TestimonialResponse>>>
}