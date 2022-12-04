package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fadlurahmanf.starterappmvvm.base.NetworkState
import com.fadlurahmanf.starterappmvvm.data.datasource.example.QuranRepository
import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.dto.response.example.BaseQuranResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahsResponse
import com.google.gson.Gson
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatcher
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ListSurahViewModelTest{
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @InjectMocks
    lateinit var viewModel: ListSurahViewModel

    @Mock
    lateinit var quranRepository: QuranRepository

    @Before
    fun setup(){
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        MockitoAnnotations.openMocks(this)
        viewModel = ListSurahViewModel(quranRepository = quranRepository)
    }

    @Test
    fun `success fetch api`(){
        Mockito.`when`(quranRepository.getSurahs("en.asad"))
            .thenReturn(Observable.just(
                BaseQuranResponse(
                    code = 200,
                    status = "OK",
                    data = SurahsResponse(
                        surahs = listOf()
                    )
                )
            ))

        viewModel.getSurahs()
        assertEquals(NetworkState.Success<List<SurahResponse>>(data = listOf()), viewModel.surahsLive.value)
    }

    @Test
    fun `failed fetch api`(){
        Mockito.`when`(quranRepository.getSurahs("en.asad"))
            .thenReturn(Observable.just(
                BaseQuranResponse(
                    code = 400,
                    status = "ERROR"
                )
            ))

        viewModel.getSurahs()
        assertEquals(Gson().toJson(NetworkState.Error(exception = CustomException(
            rawMessage = "ERROR"
        ))),  Gson().toJson(viewModel.surahsLive.value))
    }
}