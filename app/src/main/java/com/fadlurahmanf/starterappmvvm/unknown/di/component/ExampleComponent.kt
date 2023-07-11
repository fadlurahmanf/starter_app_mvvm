package com.fadlurahmanf.starterappmvvm.unknown.di.component

import com.fadlurahmanf.starterappmvvm.feature.language.presentation.ExampleLanguageActivity
import com.fadlurahmanf.starterappmvvm.feature.notification.presentation.ExampleNotificationActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.AfterLoginActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.ExampleActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.LoginActivity
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.SecondExampleActivity
import dagger.Subcomponent

@Subcomponent
interface ExampleComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create(): ExampleComponent
    }

    fun inject(activity: ExampleActivity)
    fun inject(activity: ExampleNotificationActivity)
    fun inject(activity: ExampleLanguageActivity)
    fun inject(activity: SecondExampleActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: AfterLoginActivity)
}