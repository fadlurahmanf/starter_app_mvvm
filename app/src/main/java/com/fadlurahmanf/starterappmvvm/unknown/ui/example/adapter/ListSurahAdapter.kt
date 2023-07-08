package com.fadlurahmanf.starterappmvvm.unknown.ui.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.unknown.dto.response.example.SurahResponse

class ListSurahAdapter():RecyclerView.Adapter<ListSurahAdapter.ViewHolder>() {
    var list: ArrayList<SurahResponse> = arrayListOf()

    fun setupList(list:ArrayList<SurahResponse>){
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var surahName:TextView = view.findViewById<TextView>(R.id.tv_surah_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSurahAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surah, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListSurahAdapter.ViewHolder, position: Int) {
        val surah = list[position]
        val mHolder = holder as ViewHolder

        mHolder.surahName.text = surah.name
    }

    override fun getItemCount(): Int {
        return list.size
    }
}