package com.example.mymusicplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R

class FinalAdapter constructor(val onClickListener: (Int) -> Unit) :
    ListAdapter<String, FinalAdapter.FinalViewHolder>(FinalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FinalViewHolder(inflater.inflate(R.layout.item_final, parent, false))
    }

    override fun onBindViewHolder(holder: FinalViewHolder, position: Int) {
        holder.bind(getItem(position)) {
//            holder.itemView.isSelected = !holder.itemView.isSelected
            onClickListener(position)
        }
    }

    class FinalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvPlaylist = view.findViewById<TextView>(R.id.tv_name_playlist)
        private val _view = view

        fun bind(item: String, onClickListener: (String) -> Unit) {
            tvPlaylist.text = item
            _view.setOnClickListener {
                onClickListener(item)
            }
        }
    }
}

class FinalDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = areItemsTheSame(oldItem, newItem)
}