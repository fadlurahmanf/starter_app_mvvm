package com.fadlurahmanf.starterappmvvm.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Singleton
    @Provides
    fun providesContext(application: Application):Context{
        return application
    }
}