package com.fadlurahmanf.starterappmvvm.feature.gallery.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import com.fadlurahmanf.starterappmvvm.core.domain.common.BaseActivity
import com.fadlurahmanf.starterappmvvm.databinding.ActivityExampleGalleryBinding
import com.fadlurahmanf.starterappmvvm.feature.gallery.data.dto.model.GalleryAlbumModel
import com.fadlurahmanf.starterappmvvm.feature.gallery.domain.interactor.GalleryHelper
import com.fadlurahmanf.starterappmvvm.unknown.utils.gallery.recyclerview.AlbumRecyclerView
import com.fadlurahmanf.starterappmvvm.unknown.utils.gallery.recyclerview.GalleryRecyclerView

class ExampleGalleryActivity: BaseActivity<ActivityExampleGalleryBinding>(ActivityExampleGalleryBinding::inflate) {
    private var albums:ArrayList<GalleryAlbumModel> = arrayListOf()
    private lateinit var aRecyclerView: AlbumRecyclerView
    private lateinit var gRecyclerView: GalleryRecyclerView
    override fun initSetup() {
        albums = GalleryHelper.getAll(this)
        setAdapter()
    }

    private fun setAdapter(){
        setAdapterAlbum()
        setAdapterMedia()
    }

    private fun setAdapterAlbum(){
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        aRecyclerView = AlbumRecyclerView()
        if(albums.isNotEmpty()){
            aRecyclerView.setList(albums)
        }
        binding.rvAlbum.layoutManager = lm
        binding.rvAlbum.adapter = aRecyclerView
    }

    private fun setAdapterMedia(){
        //        val lm = GridLayoutManager(this, 4)
//        recyclerView = GalleryRecyclerView()
//        if(albums.isNotEmpty()){
//            recyclerView.setList(ArrayList(albums.first().medias))
//        }
//        binding.rvGallery.layoutManager = lm
//        binding.rvGallery.adapter = recyclerView
    }

    override fun inject() {

    }
}