package com.fadlurahmanf.starterappmvvm.core.network.presentation

import android.content.Intent
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.LoginViewModel
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleLoginBinding
import javax.inject.Inject

class ExampleLoginActivity : BaseActivity<ActivityExampleLoginBinding>(ActivityExampleLoginBinding::inflate) {

    override fun initSetup() {
        observe()
        initAction()
    }

    private fun observe() {
        viewModel.loginState.observe(this) {
            when (it) {
                is CustomState.Loading -> {
                    dismissLoadingDialog()
                    showLoadingDialog()
                }

                is CustomState.Success -> {
                    dismissLoadingDialog()
                    goToAfterLoginActivity()
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

    private fun goToAfterLoginActivity() {
        val intent = Intent(this, ExampleAfterLoginActivity::class.java)
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