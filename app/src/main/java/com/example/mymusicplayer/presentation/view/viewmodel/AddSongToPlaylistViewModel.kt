package com.example.mymusicplayer.presentation.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mymusicplayer.data.MediaRepository
import com.example.mymusicplayer.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import сore.viewmodel.BaseViewModel
import javax.inject.Inject

class AddSongToPlaylistViewModel @Inject constructor(private val repository: MediaRepository) : BaseViewModel() {

    private val _songsListFlow: MutableSharedFlow<List<Song>> = MutableSharedFlow()
    val songListFlow: SharedFlow<List<Song>> = _songsListFlow

    private val songList: MutableList<Song> = mutableListOf()

    private val selectedSongList: MutableList<Song> = mutableListOf()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val songs = repository.getSongs()
            _songsListFlow.emit(songs)
            songList.addAll(songs)
        }
    }

    fun setPlaylistToSong(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addSongsPlaylist(playlistName, selectedSongList)
        }
    }

    fun setSong(song: Song) {
        selectedSongList.add(song)
    }

    fun removeSong(song: Song) {
        selectedSongList.remove(song)
    }
}