package com.fadlurahmanf.starterappmvvm.ui.example.activity

import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.base.NetworkState
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySecondExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.ui.example.viewmodel.ListSurahViewModel
import javax.inject.Inject

class SecondExampleActivity : BaseActivity<ActivitySecondExampleBinding>(ActivitySecondExampleBinding::inflate) {

    @Inject
    lateinit var viewModel:ListSurahViewModel

    override fun initSetup() {
        initObserver()
        viewModel.getSurahs()
    }

    private fun initObserver() {
        viewModel.surahsLive.observe(this){
            when(it){
                is NetworkState.Loading -> {
                    binding.tvStatus.text = "LOADING"
                }
                is NetworkState.Error -> {
                    binding.tvStatus.text = it.exception.toProperMessage(this)
                }
                is NetworkState.Success -> {
                    binding.tvStatus.text = "SUCCESS"
                }
            }
        }
    }

    private lateinit var component:ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}