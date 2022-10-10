package com.example.mymusicplayer.data

import com.example.mymusicplayer.domain.model.Song

interface MediaRepository {
    fun initMedia()
    fun getSongs(): List<Song>
    fun addSongsPlaylist(namePlaylist: String, songs: List<Song>)
    fun getPlaylists(): Set<String>
}