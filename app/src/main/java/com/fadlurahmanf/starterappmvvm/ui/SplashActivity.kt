package com.fadlurahmanf.starterappmvvm.ui

import android.os.Build
import android.os.Handler
import android.view.WindowManager
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.ui.example.ExampleActivity
import dagger.android.AndroidInjection

class SplashActivity : BaseActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun initSetup() {
        //todo
        removeStatusBar()
        Handler().postDelayed({
            ExampleActivity.newInstance(this)
            finish()
        }, 3000)
    }

    override fun initLayout() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }
}