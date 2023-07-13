package com.fadlurahmanf.starterappmvvm.feature.qris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleQrisBinding
import com.fadlurahmanf.starterappmvvm.unknown.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.unknown.ui.example.activity.QrisActivity

class ExampleQrisActivity : BaseActivity<ActivityExampleQrisBinding>(ActivityExampleQrisBinding::inflate) {

    @androidx.camera.core.ExperimentalGetImage
    override fun initSetup() {
        binding.btnQris.setOnClickListener {
            val intent = Intent(this, QrisActivity::class.java)
            startActivity(intent)
        }
    }

    private lateinit var component: ExampleComponent
    override fun inject() {
        component = appComponent.exampleComponent().create()
        component.inject(this)
    }
}