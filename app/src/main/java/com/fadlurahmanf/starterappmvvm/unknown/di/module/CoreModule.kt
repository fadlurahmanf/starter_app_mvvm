package com.fadlurahmanf.starterappmvvm.unknown.di.module

import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoRSA
import dagger.Module
import dagger.Provides
@Module
class CoreModule {

    @Provides
    fun provideCryptoRSA(): CryptoRSA {
        return CryptoRSA()
    }
}