package com.fadlurahmanf.starterappmvvm.ui.example.activity

import androidx.recyclerview.widget.GridLayoutManager
import com.fadlurahmanf.starterappmvvm.base.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityGalleryBinding
import com.fadlurahmanf.starterappmvvm.dto.model.core.AlbumGalleryModel
import com.fadlurahmanf.starterappmvvm.utils.gallery.GalleryHelper
import com.fadlurahmanf.starterappmvvm.utils.gallery.recyclerview.GalleryRecyclerView

class GalleryActivity:BaseActivity<ActivityGalleryBinding>(ActivityGalleryBinding::inflate) {
    private var albums:ArrayList<AlbumGalleryModel> = arrayListOf()
    private lateinit var recyclerView: GalleryRecyclerView
    override fun initSetup() {
        albums = GalleryHelper.getAll(this)
        setAdapter()
    }

    private fun setAdapter(){
        val lm = GridLayoutManager(this, 4)
        recyclerView = GalleryRecyclerView()
        recyclerView.setList(ArrayList(albums.first().medias))
        binding.rvGallery.layoutManager = lm
        binding.rvGallery.adapter = recyclerView
    }

    override fun inject() {

    }
}