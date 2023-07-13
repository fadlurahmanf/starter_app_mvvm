package com.fadlurahmanf.starterappmvvm.core.network.domain.interactor

import com.fadlurahmanf.starterappmvvm.core.network.domain.repository.CifRemoteDatasource
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoriteInteractor @Inject constructor(
    private var cifRemoteDatasource: CifRemoteDatasource,
){
    fun getFavorites(debugForceRefreshToken: Boolean): Observable<BaseResponse<List<FavoriteResponse>>> {
        return cifRemoteDatasource.getFavorite(debugForceRefreshToken)
    }
}