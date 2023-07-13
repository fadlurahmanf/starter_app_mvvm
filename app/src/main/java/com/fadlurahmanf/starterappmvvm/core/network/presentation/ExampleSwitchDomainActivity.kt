package com.fadlurahmanf.starterappmvvm.core.network.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.network.presentation.viewmodel.ExampleSwitchDomainViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.AppConstant
import com.fadlurahmanf.starterappmvvm.core.unknown.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleSwitchDomainBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.schedule

class ExampleSwitchDomainActivity :
    BaseActivity<ActivityExampleSwitchDomainBinding>(ActivityExampleSwitchDomainBinding::inflate) {

    @Inject
    lateinit var viewModel: ExampleSwitchDomainViewModel

    override fun initSetup() {
        initObserver()
        if (remoteConfig().getString(AppConstant.RCK.TYPE_TOKEN) == AppConstant.RCV.GUEST) {
            viewModel.getGuestToken()
        } else {
            Timer().schedule(1500) {
                val intent =
                    Intent(this@ExampleSwitchDomainActivity, ExampleLoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initObserver() {
        viewModel.guestToken.observe(this) {
            when (it) {
                is CustomState.Loading -> {
                    showLoadingDialog()
                }

                is CustomState.Success -> {
                    dismissLoadingDialog()
                    val intent =
                        Intent(this@ExampleSwitchDomainActivity, ExampleLoginActivity::class.java)
                    startActivity(intent)
                }

                is CustomState.Error -> {
                    dismissLoadingDialog()
                    showSnackBar(binding.root, it.exception.toProperMessage(this))
                }

                else -> {
                    dismissLoadingDialog()
                }
            }
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}