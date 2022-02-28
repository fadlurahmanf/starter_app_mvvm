package com.fadlurahmanf.starterappmvvm.ui.example.activity

import androidx.work.*
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.base.BaseViewState
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.data.repository.example.ExampleRepository
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.extension.observeOnce
import com.fadlurahmanf.starterappmvvm.ui.example.viewmodel.ExampleViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ExampleActivity : BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {
    lateinit var component:ExampleComponent

    @Inject
    lateinit var viewModel:ExampleViewModel

    @Inject
    lateinit var exampleRepository: ExampleRepository

    override fun initSetup() {
        initObserver()

        binding?.button1?.setOnClickListener {
            viewModel.getTestimonialState()
        }
    }


    private fun initObserver() {
//        viewModel.testimonialLoading.observe(this, { loading ->
//            if (loading){
//                showLoadingDialog()
//            }else{
//                dismissDialog()
//            }
//        })
//
//        viewModel.testimonial.observe(this, {
//            Snackbar.make(binding!!.root, "RESULT : ${it.message}", Snackbar.LENGTH_LONG).show()
//        })
//
//        viewModel.testimonialError.observeOnce(this, {
//            Snackbar.make(binding!!.root, "RESULT : ${it?:""}", Snackbar.LENGTH_LONG).show()
//        })

        viewModel.exampleState.observe(this, {
            when(it.state){
                STATE.LOADING -> {
                    showLoadingDialog()
                }
                STATE.SUCCESS -> {
                    dismissDialog()
                    Snackbar.make(binding!!.root, "RESULT : ${it.data?.message}", Snackbar.LENGTH_LONG).show()
                }
                STATE.FAILED -> {
                    dismissDialog()
                    Snackbar.make(binding!!.root, "RESULT : ${it.error}", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }


    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

}
