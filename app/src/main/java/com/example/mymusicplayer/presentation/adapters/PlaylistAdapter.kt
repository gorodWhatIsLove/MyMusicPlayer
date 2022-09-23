package com.example.mymusicplayer.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R
import com.example.mymusicplayer.domain.model.Song

class PlaylistAdapter(private val onClickListener: (String) -> Unit) :
    ListAdapter<String, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(inflater.inflate(R.layout.item_playlist, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }
    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

class PlaylistDiffCallback : DiffUtil.ItemCallback<String>()  {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = areContentsTheSame(oldItem, newItem)
}

