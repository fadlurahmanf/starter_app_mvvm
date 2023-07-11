package com.fadlurahmanf.starterappmvvm.feature.logger.presentation.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.starterappmvvm.R
import com.fadlurahmanf.starterappmvvm.feature.logger.data.dto.entity.LoggerEntity

class LogHistoryAdapter : RecyclerView.Adapter<LogHistoryAdapter.ViewHolder>() {
    private val list = arrayListOf<LoggerEntity>()

    private lateinit var context: Context

    fun setList(list: List<LoggerEntity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.list.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById<TextView>(R.id.tv_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entity = list[position]
        val text = "[${entity.date}][${entity.type}]: ${entity.message}"
        holder.textView.text = text

        when (entity.type) {
            "ERROR" -> {
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            "WARN" -> {
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            }

            "INFO" -> {
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.blue))
                holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            else -> {
                holder.textView.setTextColor(ContextCompat.getColor(context, R.color.black))
                holder.textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }
        }
    }
}