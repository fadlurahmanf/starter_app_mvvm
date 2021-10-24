package com.fadlurahmanf.starterappmvvm.di.builder

import com.fadlurahmanf.starterappmvvm.ui.ExampleActivity
import com.fadlurahmanf.starterappmvvm.ui.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindExampleActivity(): ExampleActivity

    @ContributesAndroidInjector
    abstract fun bindSplashActivity(): SplashActivity
}