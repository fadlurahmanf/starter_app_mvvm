package com.fadlurahmanf.starterappmvvm.ui.example.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.starterappmvvm.core.base.BaseViewModel
import com.fadlurahmanf.starterappmvvm.core.base.NetworkState
import com.fadlurahmanf.starterappmvvm.data.repository.example.QuranRepository
import com.fadlurahmanf.starterappmvvm.dto.exception.CustomException
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ListSurahViewModel @Inject constructor(
    var quranRepository: QuranRepository
): BaseViewModel() {

    private val _surahsLive = MutableLiveData<NetworkState<List<SurahResponse>>>()
    val surahsLive get() : LiveData<NetworkState<List<SurahResponse>>> = _surahsLive

    fun getSurahs(){
        _surahsLive.value = NetworkState.Loading
        disposable().add(quranRepository.getSurahs("en.asad")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if(it.code == 200 && it.status == "OK" && it.data?.surahs != null){
                        _surahsLive.value = NetworkState.Success(
                            data = it.data?.surahs!!
                        )
                    }else{
                        _surahsLive.value = NetworkState.Error(
                            exception = CustomException(
                                rawMessage = it.status
                            )
                        )
                    }
                },
                {
                    if(it is CustomException){
                        _surahsLive.value = NetworkState.Error(
                            exception = it
                        )
                    }else{
                        _surahsLive.value = NetworkState.Error(
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