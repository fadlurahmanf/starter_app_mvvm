package com.fadlurahmanf.starterappmvvm.di.component

import com.fadlurahmanf.starterappmvvm.ui.example.activity.FirstExampleActivity
import com.fadlurahmanf.starterappmvvm.ui.example.activity.SecondExampleActivity
import dagger.Subcomponent

@Subcomponent
interface ExampleComponent {

    @Subcomponent.Factory
    interface Factory{
        fun create():ExampleComponent
    }

    fun inject(activity: FirstExampleActivity)
    fun inject(activity: SecondExampleActivity)
}