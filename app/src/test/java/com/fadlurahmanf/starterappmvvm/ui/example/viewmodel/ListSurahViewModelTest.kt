package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fadlurahmanf.starterappmvvm.base.BaseViewState
import com.fadlurahmanf.starterappmvvm.data.entity.example.QuranDatasource
import com.fadlurahmanf.starterappmvvm.data.storage.example.QuranStorageDatasource
import com.fadlurahmanf.starterappmvvm.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse
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
class ListSurahViewModelTest {

    @get:Rule
    var rule:TestRule = InstantTaskExecutorRule()

    lateinit var listSurahViewModel: ListSurahViewModel

    @Mock
    lateinit var quranDatasource: QuranDatasource

    @Mock
    lateinit var quranStorageDatasource: QuranStorageDatasource

    @Mock
    lateinit var observerState: Observer<BaseViewState<BaseResponse<List<SurahResponse>>>>

    @Captor
    lateinit var argumentCaptor: ArgumentCaptor<BaseViewState<BaseResponse<List<SurahResponse>>>>

    @Before
    fun before(){
        MockitoAnnotations.openMocks(this)
        listSurahViewModel = ListSurahViewModel(quranDatasource, quranStorageDatasource)

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }

    @Test
    fun `get_response_code_100`(){
        var baseResponse : BaseResponse<List<SurahResponse>> = BaseResponse(code = 100, message = "OK", data = listOf())
        Mockito.`when`(listSurahViewModel.quranDatasource.getSurahs()).thenReturn(
            Observable.just(baseResponse)
        )

        listSurahViewModel.getSurahs()
        Mockito.verify(quranDatasource, Mockito.times(1)).getSurahs()

        assertEquals(100, listSurahViewModel.testimonial.value?.code)
        assertEquals("OK", listSurahViewModel.testimonial.value?.message)
    }

    @Test
    fun `get_response_code_100_state`(){
        var baseResponse : BaseResponse<List<SurahResponse>> = BaseResponse(code = 100, message = "OK")

        Mockito.`when`(listSurahViewModel.quranDatasource.getSurahs()).thenReturn(
            Observable.just(baseResponse)
        )

//        exampleViewModel.exampleState.observeForever(observer)
        listSurahViewModel.getTestimonialState()


        Mockito.verify(quranDatasource, Mockito.times(1)).getSurahs()

//        assertEquals(100, exampleViewModel.exampleState.value?.data?.code)
//        assertEquals(STATE.SUCCESS, exampleViewModel.exampleState.value?.state)
//        assertEquals(null, exampleViewModel.exampleState.value?.error)

//        exampleViewModel.exampleState.removeObserver(observer)
    }


}