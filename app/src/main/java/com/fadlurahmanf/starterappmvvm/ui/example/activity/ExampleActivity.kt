package com.fadlurahmanf.starterappmvvm.ui.example.activity

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.ui.example.adapter.ExampleAdapter
import com.fadlurahmanf.starterappmvvm.ui.example.viewmodel.ExampleViewModel
import javax.inject.Inject


class ExampleActivity : BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {
    lateinit var component:ExampleComponent

    @Inject
    lateinit var viewModel:ExampleViewModel

    private lateinit var exampleAdapter:ExampleAdapter

    override fun initSetup() {
        viewModel.getTestimonial()

        viewModel.testimonialLoading.observe(this, {
            if (it == true){
                binding.loading.visibility = View.VISIBLE
            }else{
                binding.loading.visibility = View.GONE
            }
        })

        viewModel.testimonial.observe(this, {
            setAdapter(it.data as ArrayList<TestimonialResponse>?)
        })
    }

    fun setAdapter(list: ArrayList<TestimonialResponse>?){
        exampleAdapter = ExampleAdapter(list?: arrayListOf<TestimonialResponse>())
        binding.rvTestimonial.adapter = exampleAdapter
    }

    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("RECYCLEVIEW", binding.rvTestimonial.layoutManager?.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null){
            var saved:Parcelable? = savedInstanceState.getParcelable<Parcelable>("RECYCLEVIEW")
            binding.rvTestimonial.layoutManager?.onRestoreInstanceState(saved)
        }
    }

}