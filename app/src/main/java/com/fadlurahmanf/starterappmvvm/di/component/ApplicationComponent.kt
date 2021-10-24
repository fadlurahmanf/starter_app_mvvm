package com.fadlurahmanf.starterappmvvm.di.component

import android.app.Application
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.di.builder.ActivityBuilder
import com.fadlurahmanf.starterappmvvm.di.builder.ViewModelBuilder
import com.fadlurahmanf.starterappmvvm.di.module.ApplicationModule
import com.fadlurahmanf.starterappmvvm.di.module.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ AndroidInjectionModule::class, ActivityBuilder::class,
    ApplicationModule::class, /*NetworkModule::class,*/ ViewModelFactoryModule::class, ViewModelBuilder::class])
interface ApplicationComponent {
    fun inject(app: BaseApp)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application:Application): Builder
        fun buildComponent(): ApplicationComponent
    }
}