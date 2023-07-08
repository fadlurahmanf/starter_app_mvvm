package com.fadlurahmanf.starterappmvvm.unknown.utils.gallery.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.unknown.dto.model.core.AlbumGalleryModel
import com.fadlurahmanf.starterappmvvm.unknown.dto.model.core.MediaGalleryModel

class AlbumRecyclerView():RecyclerView.Adapter<AlbumRecyclerView.ViewHolder>() {
    private var list:ArrayList<AlbumGalleryModel> = arrayListOf()

    fun setList(list:ArrayList<AlbumGalleryModel>){
        this.list.addAll(list)
        notifyItemRangeInserted(0, this.list.count())
    }

    inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val ivThumbnail:ImageView = view.findViewById<ImageView>(R.id.iv_thumbnail)
        val tvAlbumName:TextView = view.findViewById<TextView>(R.id.tv_album_name)
    }

    override fun getItemCount(): Int = list.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = list[position]

        if(album.medias.isNotEmpty()){
            Glide.with(holder.ivThumbnail).load(album.medias.first().path).into(holder.ivThumbnail)
            holder.tvAlbumName.text = album.bucketName ?: "-"
        }
    }

}

class GalleryRecyclerView():RecyclerView.Adapter<GalleryRecyclerView.ViewHolder>() {
    private var list:ArrayList<MediaGalleryModel> = arrayListOf()

    fun setList(list:ArrayList<MediaGalleryModel>){
        this.list.addAll(list)
        notifyItemRangeInserted(0, this.list.count())
    }

    inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val image:ImageView = view.findViewById<ImageView>(R.id.iv)
    }

    override fun getItemCount(): Int = list.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_video_gallery, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val media = list[position]

        Glide.with(holder.image).load(media.path).into(holder.image)
    }
}