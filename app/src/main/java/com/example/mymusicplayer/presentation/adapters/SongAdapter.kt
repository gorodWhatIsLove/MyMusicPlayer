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

class SongAdapter(val onClickListener: (Song) -> Unit) :
    ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SongViewHolder(inflater.inflate(R.layout.item_song, parent, false))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameSong: TextView = view.findViewById(R.id.tv_nameSong)
        private val artist: TextView = view.findViewById(R.id.tv_artist)
        private val time: TextView = view.findViewById(R.id.tv_time)
        private val _view = view

        fun bind(song: Song, onClickListener: (Song) -> Unit) {
            nameSong.text = song.nameSong
            artist.text = song.artist
            time.text = song.time
            _view.setOnClickListener {
                onClickListener(song)
            }
        }
    }
}

class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }

}