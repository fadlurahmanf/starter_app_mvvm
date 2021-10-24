package com.fadlurahmanf.starterappmvvm.ui

import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleBinding
import dagger.android.AndroidInjection

class ExampleActivity : BaseActivity() {
    private lateinit var binding:ActivityExampleBinding

    override fun initSetup() {
        //todo
    }

    override fun initLayout() {
        binding = ActivityExampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun inject() {
        AndroidInjection.inject(this)
    }
}