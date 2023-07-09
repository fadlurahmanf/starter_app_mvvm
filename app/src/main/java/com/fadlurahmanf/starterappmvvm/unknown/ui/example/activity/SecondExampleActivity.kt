package com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity

import android.view.View
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.databinding.ActivitySecondExampleBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.adapter.ListSurahAdapter
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.viewmodel.ListSurahViewModel
import javax.inject.Inject

class SecondExampleActivity : BaseActivity<ActivitySecondExampleBinding>(ActivitySecondExampleBinding::inflate) {

    @Inject
    lateinit var viewModel: ListSurahViewModel

    override fun initSetup() {
        initAdapter()
        initObserver()
        viewModel.getSurahs()
    }

    private lateinit var adapter: ListSurahAdapter
    private var surahs:ArrayList<SurahResponse> = arrayListOf()
    private fun initAdapter() {
        adapter = ListSurahAdapter()
        adapter.setupList(surahs)
        binding.rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun initObserver() {
        viewModel.surahsLive.observe(this){
            when(it){
                is CustomState.Loading -> {
                    binding.tvStatus.text = "LOADING"
                    binding.tvStatus.visibility = View.VISIBLE
                }
                is CustomState.Error -> {
                    binding.tvStatus.text = it.exception.toProperMessage(this)
                    binding.tvStatus.visibility = View.VISIBLE
                }
                is CustomState.Success -> {
                    binding.tvStatus.text = "SUCCESS"
                    binding.tvStatus.visibility = View.GONE
                    adapter.setupList(ArrayList(it.data))
                    adapter.notifyDataSetChanged()
                }
                else -> {}
            }
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}