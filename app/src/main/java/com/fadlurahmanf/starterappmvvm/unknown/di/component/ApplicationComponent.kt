package com.fadlurahmanf.starterappmvvm.unknown.di.component

import android.content.Context
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.unknown.di.module.CoreModule
import com.fadlurahmanf.starterappmvvm.unknown.di.module.CryptoModule
import com.fadlurahmanf.starterappmvvm.unknown.di.module.ExampleModule
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ExampleModule::class, CoreModule::class, CryptoModule::class])
interface ApplicationComponent {
    fun inject(app: BaseApp)
    fun exampleComponent(): ExampleComponent.Factory
    fun coreComponent(): CoreComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}