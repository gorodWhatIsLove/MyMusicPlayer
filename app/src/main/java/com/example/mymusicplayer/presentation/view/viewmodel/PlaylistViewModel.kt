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

class PlaylistViewModel @Inject constructor(private val repository: MediaRepository) : BaseViewModel() {

    private val _playlistFlow: MutableSharedFlow<List<String>> = MutableSharedFlow()
    val playlistFlow: SharedFlow<List<String>> = _playlistFlow

    init {
        initPlaylist()
    }

    fun initPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            _playlistFlow.emit(repository.getPlaylists().toList())
        }
    }
}