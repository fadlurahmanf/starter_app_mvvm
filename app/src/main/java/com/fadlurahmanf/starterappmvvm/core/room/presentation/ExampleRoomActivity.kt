package com.fadlurahmanf.starterappmvvm.core.room.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.room.data.dto.entity.ExampleRoomEntity
import com.fadlurahmanf.starterappmvvm.core.room.presentation.viewmodel.ExampleRoomViewModel
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleRoomBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import javax.inject.Inject

class ExampleRoomActivity :
    BaseActivity<ActivityExampleRoomBinding>(ActivityExampleRoomBinding::inflate) {

    @Inject
    lateinit var viewModel: ExampleRoomViewModel

    override fun initSetup() {
        binding.btnInsert.setOnClickListener {
            viewModel.insert(
                ExampleRoomEntity(
                    exampleString = "TES TES"
                )
            )
        }
        binding.btnGetAll.setOnClickListener {
            viewModel.getAll()
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }

}