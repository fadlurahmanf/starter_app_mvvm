package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import android.content.Intent
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.databinding.ActivityLoginBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.LoginResponse
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun initSetup() {
        observe()
        initAction()
    }

    private fun observe() {
        viewModel.loginState.observe(this){
            when(it){
                is CustomState.Loading -> {
                    dismissLoadingDialog()
                    showLoadingDialog()
                }
                is CustomState.Success -> {
                    dismissLoadingDialog()
                    goToAfterLoginActivity(it.data)
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

    private fun goToAfterLoginActivity(data: LoginResponse) {
        viewModel.saveToken(data)
        val intent = Intent(this, AfterLoginActivity::class.java)
        startActivity(intent)
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: LoginViewModel

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            viewModel.login()
        }
    }
}