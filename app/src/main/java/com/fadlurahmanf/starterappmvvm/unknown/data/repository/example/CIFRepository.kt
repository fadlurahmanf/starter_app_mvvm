package com.fadlurahmanf.starterappmvvm.unknown.data.repository.example

import com.fadlurahmanf.starterappmvvm.unknown.data.api.datasource.example.CIFDatasource
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.core.BaseResponse
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.FavoriteResponse
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CIFRepository @Inject constructor(
    private var cifDatasource: CIFDatasource,
){
    fun getFavorites(): Observable<BaseResponse<List<FavoriteResponse>>> {
        return cifDatasource.getFavorite()
    }
}