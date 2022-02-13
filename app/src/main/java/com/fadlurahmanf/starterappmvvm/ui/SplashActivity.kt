package com.fadlurahmanf.starterappmvvm.ui

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySplashBinding
import com.fadlurahmanf.starterappmvvm.ui.example.ExampleActivity
import dagger.android.AndroidInjection
import java.util.*
import kotlin.concurrent.schedule

//TES
class SplashActivity : BaseActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun initSetup() {
        //todo
        removeStatusBar()
//        Handler().postDelayed({
//            ExampleActivity.newInstance(this)
//            finish()
//        }, 3000)
        Timer().schedule(3000){
            val intent = Intent(this@SplashActivity, ExampleActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun initLayout() {
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }
}