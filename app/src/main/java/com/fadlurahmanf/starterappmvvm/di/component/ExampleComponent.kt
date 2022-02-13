package com.fadlurahmanf.starterappmvvm.di.component

import com.fadlurahmanf.starterappmvvm.ui.example.activity.ExampleActivity
import dagger.Subcomponent

@Subcomponent
interface ExampleComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create():ExampleComponent
    }

    fun inject(activity: ExampleActivity)
}