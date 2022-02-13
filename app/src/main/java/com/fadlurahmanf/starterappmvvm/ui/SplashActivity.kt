package com.fadlurahmanf.starterappmvvm.ui

import android.content.Intent
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.di.component.CoreComponent
import com.fadlurahmanf.starterappmvvm.ui.example.activity.ExampleActivity
import java.util.*
import kotlin.concurrent.schedule


class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {
    lateinit var component:CoreComponent

    override fun initSetup() {
        Timer().schedule(3000){
            val intent = Intent(this@SplashActivity, ExampleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.coreComponent().create()
        component.inject(this)
    }
}