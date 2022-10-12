package com.example.mymusicplayer.presentation.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.mymusicplayer.data.MediaRepository
import com.example.mymusicplayer.domain.model.Final
import com.example.mymusicplayer.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import сore.viewmodel.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MediaRepository) : BaseViewModel() {

    private lateinit var finalList: List<Final>

    private val _finalFlow: MutableSharedFlow<List<String>> = MutableSharedFlow()
    val finalFlow: SharedFlow<List<String>> = _finalFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initMedia()
        }
        viewModelScope.launch(Dispatchers.IO) {
            finalList = repository.getFinal()
            _finalFlow.emit(finalList.map { it.name  })
        }
    }

    fun getListSongs(): List<Song> = repository.getSongs()

//    fun getFinalsName(): List<String> {
//        return finalList.map { it.name }
//    }
}