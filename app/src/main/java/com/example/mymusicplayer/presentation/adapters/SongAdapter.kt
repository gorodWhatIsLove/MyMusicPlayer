package com.example.mymusicplayer.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymusicplayer.R
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.toTime
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val player: ExoPlayer,
    val onClickListener: (Song) -> Unit
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SongViewHolder(inflater.inflate(R.layout.item_song, parent, false))
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            Log.e("Position Song", position.toString())
            if (!player.isPlaying) {
                player.setMediaItems(getMediaItems(), position, 0)
            } else {
                player.pause()
                player.seekTo(position, 0)
            }
            player.prepare()
            player.play()
            onClickListener(currentList[position])
        }
    }

    private fun getMediaItems(): List<MediaItem> {
        return currentList.map {
            Log.e("Position Song", "Я добавил $it в список")
            MediaItem.Builder()
                .setUri(it.path.toUri())
                .setMediaMetadata(getMetadata(it))
                .build()
        }
    }

    private fun getMetadata(song: Song?): MediaMetadata =
        MediaMetadata.Builder()
            .setTitle(song?.nameSong)
            .setArtworkUri(song?.path?.toUri())
            .build()

    class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameSong: TextView = view.findViewById(R.id.tv_nameSong)
        private val artist: TextView = view.findViewById(R.id.tv_artist)
        private val time: TextView = view.findViewById(R.id.tv_time)
        private val _view = view

        fun bind(song: Song) {
            nameSong.text = song.nameSong
            artist.text = song.artist
            time.text = song.duration.toTime()
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