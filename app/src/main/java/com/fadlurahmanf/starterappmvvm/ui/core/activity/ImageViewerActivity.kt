package com.fadlurahmanf.starterappmvvm.ui.core.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.data.model.ImageModel
import com.fadlurahmanf.starterappmvvm.data.model.ImageOrigin
import com.fadlurahmanf.starterappmvvm.databinding.ActivityImageViewerBinding
import com.ortiz.touchview.OnTouchImageViewListener

class ImageViewerActivity : BaseActivity<ActivityImageViewerBinding>(ActivityImageViewerBinding::inflate) {

    companion object{
        const val IMAGE = "IMAGE"
    }

    private var image:ImageModel? = null

    override fun initSetup() {
        image = intent.getParcelableExtra<ImageModel>(IMAGE)

        if(image == null){
            return
        }

        if(image!!.origin == ImageOrigin.URI){
            Glide.with(binding.photoView)
                .load(Uri.parse(image!!.path))
                .centerCrop()
                .into(binding.photoView)
        }else{
            Glide.with(binding.photoView)
                .load(Uri.parse("https://i.picsum.photos/id/283/200/300.jpg?hmac=HVbRBUPQVx2vypDRbrSdilx6LhDFbU9jsqNdlKR9J9I"))
                .centerCrop()
                .into(binding.photoView)
        }
    }

    override fun inject() {

    }
}