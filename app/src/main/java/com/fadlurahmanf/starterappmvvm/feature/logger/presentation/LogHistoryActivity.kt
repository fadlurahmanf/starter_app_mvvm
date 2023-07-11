package com.fadlurahmanf.starterappmvvm.feature.logger.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityLogHistoryBinding
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.rv.LogHistoryAdapter
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.viewmodel.LoggerViewModel
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import javax.inject.Inject

class LogHistoryActivity :
    BaseActivity<ActivityLogHistoryBinding>(ActivityLogHistoryBinding::inflate) {

    @Inject
    lateinit var viewModel: LoggerViewModel

    private lateinit var adapter: LogHistoryAdapter
    private val list = arrayListOf<String>()

    override fun initSetup() {
        adapter = LogHistoryAdapter()
        adapter.setList(list)
        val lm = LinearLayoutManager(this)
        binding.rv.layoutManager = lm
        binding.rv.adapter = adapter

        initObserver()
        viewModel.getAllHistory()
    }

    private fun initObserver() {
        viewModel.state.observe(this) {
            logConsole.d("MASUK $it")
            when (it) {
                is CustomState.Success -> {
                    adapter.setList(it.data)
                }

                is CustomState.Error -> {
                    logConsole.e("MASUK ERROR: ${it.exception}")
                }
                else -> {

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