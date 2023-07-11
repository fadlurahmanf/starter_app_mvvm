package com.fadlurahmanf.starterappmvvm.feature.logger.presentation.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fadlurahmanf.starterappmvvm.R

class LogHistoryAdapter : RecyclerView.Adapter<LogHistoryAdapter.ViewHolder>() {
    private val list = arrayListOf<String>()

    private lateinit var context: Context

    fun setList(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyItemRangeInserted(0, list.size - 1)
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
        val text = list[position]
        holder.textView.text = text

        if (text.contentEquals("[ERROR]")) {
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else if (text.contentEquals("[WARN]")) {
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.yellow))
        } else if (text.contentEquals("[INFO]")) {
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.blue))
        }
    }
}