package com.fadlurahmanf.starterappmvvm.ui.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.dto.response.example.SurahResponse
import com.fadlurahmanf.starterappmvvm.databinding.ItemTestimonialBinding

class ExampleAdapter(var list: ArrayList<SurahResponse>):RecyclerView.Adapter<ExampleAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        var tvQuotes:TextView = view.findViewById<TextView>(R.id.tv_quotes)
    }

    lateinit var binding: ItemTestimonialBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleAdapter.ViewHolder {
        binding = ItemTestimonialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ExampleAdapter.ViewHolder, position: Int) {
        var testimonial = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}