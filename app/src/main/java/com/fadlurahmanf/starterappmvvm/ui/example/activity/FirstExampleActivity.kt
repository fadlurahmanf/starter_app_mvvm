package com.fadlurahmanf.starterappmvvm.ui.example.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.base.STATE
import com.fadlurahmanf.starterappmvvm.core.utilities.CryptoHelper
import com.fadlurahmanf.starterappmvvm.data.model.ImageModel
import com.fadlurahmanf.starterappmvvm.data.model.ImageOrigin
import com.fadlurahmanf.starterappmvvm.data.model.PdfModel
import com.fadlurahmanf.starterappmvvm.data.model.PdfOrigin
import com.fadlurahmanf.starterappmvvm.data.storage.example.ExampleSpStorage
import com.fadlurahmanf.starterappmvvm.databinding.ActivityFirstExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.ui.core.activity.ImageViewerActivity
import com.fadlurahmanf.starterappmvvm.ui.core.activity.PdfViewerActivity
import com.fadlurahmanf.starterappmvvm.ui.example.viewmodel.ExampleViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*
import javax.inject.Inject


class FirstExampleActivity : BaseActivity<ActivityFirstExampleBinding>(ActivityFirstExampleBinding::inflate) {
    lateinit var component:ExampleComponent

    @Inject
    lateinit var viewModel:ExampleViewModel

    @Inject
    lateinit var exampleSpStorage: ExampleSpStorage

    override fun initSetup() {
        initObserver()

        binding.button2.setOnClickListener {
            viewModel.getTestimonialState()
        }

        binding.button1.setOnClickListener {
            val encrypted = CryptoHelper.encrypt("tes tes")
            println("masuk encrypted $encrypted")
            val decrypted = CryptoHelper.decrypt(encrypted?:"")
            println("masuk decrypted $decrypted")
        }

        binding.buttonPickPdf.setOnClickListener {
            val intent = Intent()
                .setType("application/pdf")
                .setAction(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

        binding.buttonPdf.setOnClickListener {
            val intent = Intent(this, PdfViewerActivity::class.java)
            startActivity(intent)
        }

        /**
         * reference: https://developer.android.com/training/data-storage/shared/photopicker
         * */
        binding.buttonPickImageFromGallery.setOnClickListener {
            pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonImageViewer.setOnClickListener {
            val intent = Intent(this, ImageViewerActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 111){
            val uri = data!!.data

            val intent = Intent(this, PdfViewerActivity::class.java)
            intent.putExtra(PdfViewerActivity.PDF, PdfModel(origin = PdfOrigin.FILE, path = uri.toString()))
            startActivity(intent)
        }
    }

    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ data ->
        data?.let {
            val intent = Intent(this, ImageViewerActivity::class.java)
            intent.putExtra(ImageViewerActivity.IMAGE, ImageModel(origin = ImageOrigin.URI, it.toString()))
            startActivity(intent)
        }
    }


    private fun initObserver() {

        viewModel.exampleState.observe(this) {
            when (it.state) {
                STATE.LOADING -> {
                    showLoadingDialog()
                }
                STATE.SUCCESS -> {
                    dismissDialog()
                    Snackbar.make(
                        binding.root,
                        "RESULT : ${it.data?.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                STATE.FAILED -> {
                    dismissDialog()
                    Snackbar.make(binding.root, "RESULT : ${it.error}", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

}
