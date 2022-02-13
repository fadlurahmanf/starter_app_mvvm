package com.fadlurahmanf.starterappmvvm.di.component

import com.fadlurahmanf.starterappmvvm.ui.SplashActivity
import dagger.Subcomponent

@Subcomponent
interface CoreComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create():CoreComponent
    }

    fun inject(activity:SplashActivity)
}