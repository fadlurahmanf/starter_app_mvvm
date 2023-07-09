package com.fadlurahmanf.starterappmvvm.unknown.ui.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.data.repository.example.QuranRepository
import com.fadlurahmanf.starterappmvvm.unknown.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ListSurahViewModel @Inject constructor(
    var quranRepository: QuranRepository
): BaseViewModel() {

    private val _surahsLive = MutableLiveData<CustomState<List<SurahResponse>>>()
    val surahsLive get() : LiveData<CustomState<List<SurahResponse>>> = _surahsLive

    fun getSurahs(){
        _surahsLive.value = CustomState.Loading
        disposable().add(quranRepository.getSurahs("en.asad")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if(it.code == 200 && it.status == "OK" && it.data?.surahs != null){
                        _surahsLive.value = CustomState.Success(
                            data = it.data?.surahs!!
                        )
                    }else{
                        _surahsLive.value = CustomState.Error(
                            exception = CustomException(
                                rawMessage = it.status
                            )
                        )
                    }
                },
                {
                    if(it is CustomException){
                        _surahsLive.value = CustomState.Error(
                            exception = it
                        )
                    }else{
                        _surahsLive.value = CustomState.Error(
                            exception = CustomException(
                                rawMessage = it.message
                            )
                        )
                    }
                },
                {}
            ))
    }
}