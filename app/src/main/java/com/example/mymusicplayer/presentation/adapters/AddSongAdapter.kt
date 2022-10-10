package com.example.mymusicplayer.presentation.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.mymusicplayer.R
import com.example.mymusicplayer.domain.model.Song

class AddSongAdapter constructor(val onClickListener: (Song, Boolean) -> Unit) :
    ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SongAdapter.SongViewHolder(inflater.inflate(R.layout.item_song, parent, false))
    }

    override fun onBindViewHolder(holder: SongAdapter.SongViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            it.isSelected = !it.isSelected
            onClickListener(currentList[position], it.isSelected)
        }
    }
}
