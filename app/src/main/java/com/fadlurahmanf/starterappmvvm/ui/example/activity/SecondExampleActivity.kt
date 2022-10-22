package com.fadlurahmanf.starterappmvvm.ui.example.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySecondExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.ui.example.viewmodel.ExampleViewModel
import javax.inject.Inject

class SecondExampleActivity : BaseActivity<ActivitySecondExampleBinding>(ActivitySecondExampleBinding::inflate) {

    @Inject
    lateinit var viewModel:ExampleViewModel

    override fun initSetup() {
        initObserver()
        viewModel.getTestimonial()
    }

    private fun initObserver() {
        viewModel.exampleState.observe(this){
            if (it.state == STATE.FAILED){
                binding.tvStatus.text = "FAILED"
            }else if(it.state == STATE.LOADING){
                binding.tvStatus.text = "LOADING"
            }else if (it.state == STATE.SUCCESS){
                binding.tvStatus.text = "SUCCESS"
            }
        }
    }

    private lateinit var component:ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}