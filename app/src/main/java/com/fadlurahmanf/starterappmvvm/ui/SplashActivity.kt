package com.fadlurahmanf.starterappmvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.di.component.ApplicationComponent
import com.fadlurahmanf.starterappmvvm.di.component.CoreComponent


class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    lateinit var component:CoreComponent

    override fun initSetup() {
    }

    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.coreComponent().create()
        component.inject(this)
    }
}