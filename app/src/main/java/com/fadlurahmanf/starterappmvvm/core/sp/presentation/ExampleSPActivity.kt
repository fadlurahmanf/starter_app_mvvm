package com.fadlurahmanf.starterappmvvm.core.sp.presentation

import com.fadlurahmanf.starterappmvvm.core.unknown.data.constant.logConsole
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleSpBinding
import com.fadlurahmanf.starterappmvvm.core.sp.data.dto.model.ExampleModelSp
import com.fadlurahmanf.starterappmvvm.core.sp.data.storage.ExampleSpStorage
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import javax.inject.Inject

class ExampleSPActivity :
    BaseActivity<ActivityExampleSpBinding>(ActivityExampleSpBinding::inflate) {

    @Inject
    lateinit var exampleSpStorage: ExampleSpStorage

    override fun initSetup() {
        binding.btnSave.setOnClickListener {
            exampleSpStorage.exampleString = "EXAMPLE STRING"
            exampleSpStorage.exampleInt = 121
            exampleSpStorage.exampleFloat = 91f
            exampleSpStorage.exampleLong = 1L
            exampleSpStorage.exampleData = ExampleModelSp(
                string = "TES",
                int = 2,
                float = 5f,
                long = 1L
            )
            exampleSpStorage.exampleListData = arrayListOf(
                ExampleModelSp(
                    string = "TES",
                    int = 2,
                    float = 5f,
                    long = 1L
                ),
                ExampleModelSp(
                    string = "TES",
                    int = 2,
                    float = 5f,
                    long = 1L
                )
            )
        }

        binding.btnSee.setOnClickListener {
            logConsole.d("EXAMPLE_STRING: ${exampleSpStorage.exampleString}")
            logConsole.d("EXAMPLE_INT: ${exampleSpStorage.exampleInt}")
            logConsole.d("EXAMPLE_LONG: ${exampleSpStorage.exampleLong}")
            logConsole.d("EXAMPLE_FLOAT: ${exampleSpStorage.exampleFloat}")
            logConsole.d("EXAMPLE_DATA: ${exampleSpStorage.exampleData}")
            logConsole.d("EXAMPLE_LIST_DATA: ${exampleSpStorage.exampleListData}")

            logConsole.d("EXAMPLE_STRING RAW: ${exampleSpStorage.getRawExampleString()}")
            logConsole.d("EXAMPLE_INT RAW: ${exampleSpStorage.getRawExampleInt()}")
            logConsole.d("EXAMPLE_LONG RAW: ${exampleSpStorage.getRawExampleLong()}")
            logConsole.d("EXAMPLE_FLOAT RAW: ${exampleSpStorage.getRawExampleFloat()}")
            logConsole.d("EXAMPLE_DATA RAW: ${exampleSpStorage.getRawExampleData()}")
            logConsole.d("EXAMPLE_LIST_DATA RAW: ${exampleSpStorage.getRawExampleListData()}")
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }
}