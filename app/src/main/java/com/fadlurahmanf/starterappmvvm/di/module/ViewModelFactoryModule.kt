package com.fadlurahmanf.starterappmvvm.di.module

import androidx.lifecycle.ViewModelProvider
import com.fadlurahmanf.starterappmvvm.di.builder.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}