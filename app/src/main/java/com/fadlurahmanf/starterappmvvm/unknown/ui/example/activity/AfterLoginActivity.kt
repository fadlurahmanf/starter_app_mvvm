package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.databinding.ActivityAfterLoginBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.viewmodel.AfterLoginViewModel
import javax.inject.Inject

class AfterLoginActivity : BaseActivity<ActivityAfterLoginBinding>(ActivityAfterLoginBinding::inflate) {
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