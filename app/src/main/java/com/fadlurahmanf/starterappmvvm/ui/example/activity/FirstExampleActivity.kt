package com.fadlurahmanf.starterappmvvm.ui.example.activity

import android.content.Intent
import com.fadlurahmanf.starterappmvvm.BaseApp
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.core.helper.CryptoHelper
import com.fadlurahmanf.starterappmvvm.core.helper.TranslationHelper
import com.fadlurahmanf.starterappmvvm.data.storage.language.LanguageSpStorage
import com.fadlurahmanf.starterappmvvm.dto.model.core.ImageModel
import com.fadlurahmanf.starterappmvvm.dto.model.core.ImageOrigin
import com.fadlurahmanf.starterappmvvm.dto.model.PdfModel
import com.fadlurahmanf.starterappmvvm.dto.model.PdfOrigin
import com.fadlurahmanf.starterappmvvm.databinding.ActivityFirstExampleBinding
import com.fadlurahmanf.starterappmvvm.di.component.ExampleComponent
import com.fadlurahmanf.starterappmvvm.ui.core.activity.ImageViewerActivity
import com.fadlurahmanf.starterappmvvm.ui.core.activity.PdfViewerActivity
import javax.inject.Inject


class FirstExampleActivity : BaseActivity<ActivityFirstExampleBinding>(ActivityFirstExampleBinding::inflate) {
    lateinit var component:ExampleComponent

    @Inject
    lateinit var languageSpStorage: LanguageSpStorage

    override fun initSetup() {
        binding.btnChangeLanguage.setOnClickListener {
            val local = TranslationHelper.getCurrentLocale(this)
            if(local.language == "en"){
                TranslationHelper.changeLanguage(this, "in")
                languageSpStorage.languageId = "in"
            }else{
                TranslationHelper.changeLanguage(this, "en")
                languageSpStorage.languageId = "in"
            }
            recreate()
        }


        binding.button2.setOnClickListener {
            val intent = Intent(this, SecondExampleActivity::class.java)
            startActivity(intent)
        }

        binding.button1.setOnClickListener {
            val encrypted = CryptoHelper.encrypt("tes tes")
            val decrypted = CryptoHelper.decrypt(encrypted?:"")
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
            // alternative:
            // pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            val intent = Intent()
                .setType("image/*")
                // .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) /** add this line if you want to pick multiple */
                .setAction(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Select Multiple File"), 121)
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
        }else if (resultCode == RESULT_OK && requestCode == 121){
            data?.data?.let {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putExtra(ImageViewerActivity.IMAGE, ImageModel(origin = ImageOrigin.URI, path = it.toString()))
                startActivity(intent)
            }

//            for (i in 0 until data!!.clipData!!.itemCount) {
//                val uri = data.clipData!!.getItemAt(i)
//            }
        }
    }

//    private val pickMediaLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ data ->
//        data?.let {
//            val intent = Intent(this, ImageViewerActivity::class.java)
//            intent.putExtra(ImageViewerActivity.IMAGE, ImageModel(origin = ImageOrigin.URI, it.toString()))
//            startActivity(intent)
//        }
//    }


    override fun inject() {
        component = (applicationContext as BaseApp).applicationComponent.exampleComponent().create()
        component.inject(this)
    }

}
