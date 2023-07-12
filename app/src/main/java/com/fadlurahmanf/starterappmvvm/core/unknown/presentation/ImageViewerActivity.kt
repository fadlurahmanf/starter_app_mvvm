package com.fadlurahmanf.starterappmvvm.core.unknown.presentation

import android.net.Uri
import com.bumptech.glide.Glide
import com.fadlurahmanf.starterappmvvm.core.unknown.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityImageViewerBinding
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.ImageModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.constant.ImageOrigin

class ImageViewerActivity :
    BaseActivity<ActivityImageViewerBinding>(ActivityImageViewerBinding::inflate) {

    companion object {
        const val IMAGE = "IMAGE"
    }

    private var image: ImageModel? = null

    override fun initSetup() {
        image = intent.getParcelableExtra<ImageModel>(IMAGE)

        if (image == null) {
            return
        }

        if (image!!.origin == ImageOrigin.URI) {
            Glide.with(binding.photoView)
                .load(Uri.parse(image!!.path))
                .centerCrop()
                .into(binding.photoView)
        } else {
            Glide.with(binding.photoView)
                .load(Uri.parse("https://i.picsum.photos/id/283/200/300.jpg?hmac=HVbRBUPQVx2vypDRbrSdilx6LhDFbU9jsqNdlKR9J9I"))
                .centerCrop()
                .into(binding.photoView)
        }
    }

    override fun inject() {

    }
}