package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fadlurahmanf.starterappmvvm.base.BaseViewState
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.data.entity.example.ExampleEntity
import com.fadlurahmanf.starterappmvvm.data.repository.example.ExampleRepository
import com.fadlurahmanf.starterappmvvm.data.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ExampleViewModelTest {

    @get:Rule
    var rule:TestRule = InstantTaskExecutorRule()

    lateinit var exampleViewModel: ExampleViewModel

    @Mock
    lateinit var exampleEntity: ExampleEntity

    @Mock
    lateinit var exampleRepository: ExampleRepository

    @Mock
    lateinit var observerState: Observer<BaseViewState<BaseResponse<List<TestimonialResponse>>>>

    @Captor
    lateinit var argumentCaptor: ArgumentCaptor<BaseViewState<BaseResponse<List<TestimonialResponse>>>>

    @Before
    fun before(){
        MockitoAnnotations.openMocks(this)
        exampleViewModel = ExampleViewModel(exampleEntity, exampleRepository)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }

    @Test
    fun `get_response_code_100`(){
        var baseResponse : BaseResponse<List<TestimonialResponse>> = BaseResponse(code = 100, message = "OK", data = listOf())
        Mockito.`when`(exampleViewModel.exampleEntity.getTestimonial()).thenReturn(
            Observable.just(baseResponse)
        )

        exampleViewModel.getTestimonial()
        Mockito.verify(exampleEntity, Mockito.times(1)).getTestimonial()

        assertEquals(100, exampleViewModel.testimonial.value?.code)
        assertEquals("OK", exampleViewModel.testimonial.value?.message)
    }

    @Test
    fun `get_response_code_100_state`(){
        var baseResponse : BaseResponse<List<TestimonialResponse>> = BaseResponse(code = 100, message = "OK")

        Mockito.`when`(exampleViewModel.exampleEntity.getTestimonial()).thenReturn(
            Observable.just(baseResponse)
        )

//        exampleViewModel.exampleState.observeForever(observer)
        exampleViewModel.getTestimonialState()


        Mockito.verify(exampleEntity, Mockito.times(1)).getTestimonial()

//        assertEquals(100, exampleViewModel.exampleState.value?.data?.code)
//        assertEquals(STATE.SUCCESS, exampleViewModel.exampleState.value?.state)
//        assertEquals(null, exampleViewModel.exampleState.value?.error)

//        exampleViewModel.exampleState.removeObserver(observer)
    }


}