package com.fadlurahmanf.starterappmvvm.ui.example.activity

import android.content.Context
import android.view.View
import androidx.work.*
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.data.repository.example.ExampleRepository
import com.fadlurahmanf.starterappmvvm.data.response.example.TestimonialResponse
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.extension.observeOnce
import com.fadlurahmanf.starterappmvvm.ui.example.adapter.ExampleAdapter
import com.fadlurahmanf.starterappmvvm.ui.example.viewmodel.ExampleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ExampleActivity : BaseActivity<ActivityExampleBinding>(ActivityExampleBinding::inflate) {
    lateinit var component:ExampleComponent

    @Inject
    lateinit var viewModel:ExampleViewModel

    @Inject
    lateinit var exampleRepository: ExampleRepository

    private lateinit var exampleAdapter:ExampleAdapter

    var listTestimonial = arrayListOf<TestimonialResponse>()

    override fun initSetup() {
        initAdapter(listTestimonial)
        viewModel.getTestimonial()
        initObserver()


        binding.tv.setOnClickListener {
            var oneTimeWorkRequest : OneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java).build()
            exampleRepository.uuidString = oneTimeWorkRequest.id.toString()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
            observeWork(oneTimeWorkRequest.id)
        }
        exampleRepository.uuidString = null
        if (exampleRepository.uuidString!=null){
            observeWork(UUID.fromString(exampleRepository.uuidString))
        }
    }

    private fun initObserver() {
        viewModel.testimonialLoading.observe(this, { loading ->
            if (loading){
                binding.loading.visibility = View.VISIBLE
                binding.rvTestimonial.visibility = View.GONE
            }else{
                binding.loading.visibility = View.GONE
            }
        })

        viewModel.testimonial.observe(this, {
            binding.rvTestimonial.visibility = View.VISIBLE
            refreshRecycleView((it.data as ArrayList<TestimonialResponse>?)?: arrayListOf())
        })

        viewModel.testimonialError.observeOnce(this, {
            binding.rvTestimonial.visibility = View.GONE
            binding.loading.visibility = View.GONE
        })
    }

    private fun observeWork(id:UUID){
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(id).observe(this, {
            val task = it?.outputData?.getString("task")
            if (task == "BENER"){
                println("MASUK ${task}")
                exampleRepository.uuidString = null
                binding.loading.visibility = View.GONE
            }else{
                println("MASUK ${task}")
                binding.loading.visibility = View.VISIBLE
            }
        })
    }

    private fun initAdapter(list: ArrayList<TestimonialResponse>){
        exampleAdapter = ExampleAdapter(listTestimonial)
        binding.rvTestimonial.adapter = exampleAdapter
    }

    private fun refreshRecycleView(list: ArrayList<TestimonialResponse>){
        listTestimonial.clear()
        listTestimonial.addAll(list)
        exampleAdapter.notifyDataSetChanged()
    }

    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

}


class DownloadWorker (
    val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        try {
            runBlocking {
                delay(20000)
            }
            var outputData = workDataOf("task" to "BENER")
            return Result.success(outputData)
        }catch (e:Exception){
            return Result.failure()
        }
    }
}
