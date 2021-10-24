package com.fadlurahmanf.starterappmvvm

import android.app.Application
import com.fadlurahmanf.starterappmvvm.di.component.ApplicationComponent
import com.fadlurahmanf.starterappmvvm.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BaseApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    lateinit var applicationComponent: ApplicationComponent

    override fun androidInjector(): AndroidInjector<Any> {
        return  androidInjector
    }

    override fun onCreate() {
        super.onCreate()
        initInjection()
    }

    private fun initInjection(){
        applicationComponent = DaggerApplicationComponent.builder().application(this).buildComponent()
        applicationComponent.inject(this)
    }
}