package com.example.mymusicplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R

class PlaylistFinalAdapter(private val onClickListener: (String, Boolean) -> Unit) :
    ListAdapter<String, PlaylistFinalAdapter.PlaylistFinalViewHolder>(PlaylistDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFinalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlaylistFinalViewHolder(inflater.inflate(R.layout.item_playlist, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistFinalViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            holder.itemView.isSelected = !holder.itemView.isSelected
            onClickListener(it, holder.itemView.isSelected)
        }
    }

    fun resetSelected() {

    }

    class PlaylistFinalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvPlaylist = view.findViewById<TextView>(R.id.tv_playlist)
        private val _view = view

        fun bind(item: String, onClickListener: (String) -> Unit) {
            tvPlaylist.text = item
            _view.setOnClickListener {
                onClickListener(item)
            }
        }
    }
}