package com.fadlurahmanf.starterappmvvm.ui.example

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.base.BaseMvvmActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleBinding
import com.fadlurahmanf.starterappmvvm.di.builder.ViewModelFactory
import dagger.android.AndroidInjection
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class ExampleActivity : BaseMvvmActivity() {
    private lateinit var binding: ActivityExampleBinding
    private lateinit var viewModel: ExampleViewModel

    companion object{
        fun newInstance(activity:Activity){
            activity.startActivity<ExampleActivity>()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun initMVVM() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(ExampleViewModel::class.java)
    }

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