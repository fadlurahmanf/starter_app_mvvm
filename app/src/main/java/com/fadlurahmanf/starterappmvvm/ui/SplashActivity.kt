package com.fadlurahmanf.starterappmvvm.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import dagger.android.AndroidInjection

class SplashActivity : BaseActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun initSetup() {
        //todo
    }

    override fun initLayout() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }
}