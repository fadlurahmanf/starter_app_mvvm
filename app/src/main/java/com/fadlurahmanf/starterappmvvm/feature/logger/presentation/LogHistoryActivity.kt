package com.fadlurahmanf.starterappmvvm.feature.logger.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import com.fadlurahmanf.starterappmvvm.core.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.data.state.CustomState
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityLogHistoryBinding
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.rv.LogHistoryAdapter
import com.fadlurahmanf.starterappmvvm.feature.logger.presentation.viewmodel.LoggerViewModel
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import javax.inject.Inject

class LogHistoryActivity :
    BaseActivity<ActivityLogHistoryBinding>(ActivityLogHistoryBinding::inflate) {

    @Inject
    lateinit var viewModel: LoggerViewModel

    private lateinit var adapter: LogHistoryAdapter
    private val list = arrayListOf<LoggerEntity>()
    private val displayedList = arrayListOf<LoggerEntity>()

    override fun initSetup() {
        adapter = LogHistoryAdapter()
        adapter.setList(displayedList)
        val lm = LinearLayoutManager(this)
        binding.rv.layoutManager = lm
        binding.rv.adapter = adapter

        initObserver()
        initAction()
        viewModel.getAllHistory()
//        viewModel.deleteLogs()
    }

    private fun initAction() {
        binding.tvDebug.setOnClickListener { _ ->
            displayedList.clear()
            displayedList.addAll(list.filter {
                it.type == "DEBUG"
            }.toList())
            adapter.setList(displayedList)
        }

        binding.tvError.setOnClickListener {
            displayedList.clear()
            displayedList.addAll(list.filter {
                it.type == "ERROR"
            }.toList())
            adapter.setList(displayedList)
        }

        binding.tvInfo.setOnClickListener {
            displayedList.clear()
            displayedList.addAll(list.filter {
                it.type == "INFO"
            }.toList())
            adapter.setList(displayedList)
        }

        binding.tvWarn.setOnClickListener {
            displayedList.clear()
            displayedList.addAll(list.filter {
                it.type == "WARN"
            }.toList())
            adapter.setList(displayedList)
        }
    }

    private fun initObserver() {
        viewModel.state.observe(this) {
            when (it) {
                is CustomState.Success -> {
                    list.addAll(it.data)
                    displayedList.addAll(it.data)
                    adapter.setList(displayedList)
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