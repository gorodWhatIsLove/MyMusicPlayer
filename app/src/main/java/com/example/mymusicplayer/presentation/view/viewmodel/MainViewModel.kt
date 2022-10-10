package com.example.mymusicplayer.presentation.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mymusicplayer.data.MediaRepository
import com.example.mymusicplayer.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import сore.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MediaRepository) : BaseViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initMedia()
        }
    }

    fun getListSongs(): List<Song> = repository.getSongs()

}