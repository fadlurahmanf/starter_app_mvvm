package com.fadlurahmanf.starterappmvvm.di.component

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.di.module.CoreModule
import com.fadlurahmanf.starterappmvvm.di.module.ExampleModule
import dagger.BindsInstance
import dagger.Component


@Component(modules = [ExampleModule::class, CoreModule::class])
interface ApplicationComponent {
    fun inject(app: BaseApp)
    fun exampleComponent():ExampleComponent.Factory
    fun coreComponent():CoreComponent.Factory

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) : ApplicationComponent
    }
}