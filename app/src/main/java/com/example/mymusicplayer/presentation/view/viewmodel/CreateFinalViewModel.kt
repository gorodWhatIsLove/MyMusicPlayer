package com.example.mymusicplayer.presentation.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusicplayer.data.MediaRepository
import com.example.mymusicplayer.domain.model.Final
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateFinalViewModel @Inject constructor(val repository: MediaRepository) : ViewModel() {

    var counterDance: Int = 0

    var nameFinal: String = ""

    var durationDance: String = ""

    var durationPause: String = ""

    var playlistList: MutableList<String> = mutableListOf()

    private val _counterDanceFlow: MutableSharedFlow<Int> = MutableSharedFlow()
    val counterDanceFlow: SharedFlow<Int> = _counterDanceFlow

    private val _playlistFlow: MutableSharedFlow<List<String>> = MutableSharedFlow()
    val playlistFlow: SharedFlow<List<String>> = _playlistFlow

    init {
        viewModelScope.launch {
            _counterDanceFlow.emit(counterDance)
        }
        initPlaylist()
    }

    fun changeCountDance() {
        viewModelScope.launch {
            _counterDanceFlow.emit(counterDance)
        }
    }

    private fun initPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            _playlistFlow.emit(repository.getPlaylists().toList())
        }
    }

    fun createFinalModel(): Final {
        val durationDanceInt = (durationDance.toDouble() * 60).toInt()
        val durationPauseInt = durationPause.toInt()
        return Final(nameFinal, durationDanceInt, durationPauseInt, playlistList)
    }

}