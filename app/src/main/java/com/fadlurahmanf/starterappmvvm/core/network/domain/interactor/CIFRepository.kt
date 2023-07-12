package com.fadlurahmanf.starterappmvvm.core.network.domain.interactor

import com.fadlurahmanf.starterappmvvm.core.network.domain.repository.CIFRemoteDatasource
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CIFRepository @Inject constructor(
    private var cifRemoteDatasource: CIFRemoteDatasource,
){
    fun getFavorites(): Observable<BaseResponse<List<FavoriteResponse>>> {
        return cifRemoteDatasource.getFavorite()
    }
}