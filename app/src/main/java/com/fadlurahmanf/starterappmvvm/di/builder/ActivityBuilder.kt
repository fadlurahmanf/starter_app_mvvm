package com.fadlurahmanf.starterappmvvm.di.builder

import com.fadlurahmanf.starterappmvvm.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}