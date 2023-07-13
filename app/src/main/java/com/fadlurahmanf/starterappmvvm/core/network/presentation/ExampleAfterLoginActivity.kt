package com.fadlurahmanf.starterappmvvm.core.network.presentation

import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.AfterLoginViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseAfterLoginActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleAfterLoginBinding
import javax.inject.Inject

class ExampleAfterLoginActivity : BaseAfterLoginActivity<ActivityExampleAfterLoginBinding>(ActivityExampleAfterLoginBinding::inflate) {
    override fun initSetup() {
        observe()
        initAction()
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: AfterLoginViewModel

    override fun inject1() {

    }

    private fun observe(){
        viewModel.favoriteState.observe(this){
            when(it){
                is CustomState.Loading -> {
                    dismissLoadingDialog()
                    showLoadingDialog()
                }
                is CustomState.Success -> {
                    dismissLoadingDialog()
                    showSnackBar(binding.root, message = "SUKSES")
                }
                is CustomState.Error -> {
                    dismissLoadingDialog()
                    showSnackBar(binding.root, message = it.exception.toProperMessage(this))
                }
                else -> {
                    dismissLoadingDialog()
                }
            }
        }
    }

    private fun initAction(){
        binding.btnGetFavorite.setOnClickListener {
            viewModel.getFavorite()
        }
    }

}