package com.fadlurahmanf.starterappmvvm.unknown.di.component


import com.fadlurahmanf.starterappmvvm.unknown.ui.SplashActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.core.dialog.DefaultLoadingDialog
import dagger.Subcomponent

@Subcomponent
interface CoreComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CoreComponent
    }

    fun inject(activity: SplashActivity)
    fun inject(dialog: DefaultLoadingDialog)
}