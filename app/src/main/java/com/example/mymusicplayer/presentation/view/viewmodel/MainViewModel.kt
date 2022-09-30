package com.example.mymusicplayer.presentation.view.viewmodel

import com.example.mymusicplayer.data.MediaRepository
import com.example.mymusicplayer.domain.model.Song
import сore.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MediaRepository): BaseViewModel() {

    init {
        repository.initMedia()
    }

    fun getListSongs(): List<Song> = repository.getSongs()

}