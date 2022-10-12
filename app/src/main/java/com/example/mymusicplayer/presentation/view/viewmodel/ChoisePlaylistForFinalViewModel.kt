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

class ChoisePlaylistForFinalViewModel @Inject constructor(val repository: MediaRepository) : ViewModel() {

    var finalModel: Final? = null

    var addFinalList: MutableList<String> = mutableListOf()

    var deletePlaylistIndex: Int = -1

    private val finalList: MutableList<String> = mutableListOf()

    private val _finalListFlow: MutableSharedFlow<List<String>> = MutableSharedFlow()
    val finalListFlow: SharedFlow<List<String>> = _finalListFlow

    fun addPlaylistForFinal() {
        finalList.addAll(addFinalList)
        addFinalList.clear()
        viewModelScope.launch {
            _finalListFlow.emit(finalList)
        }
    }

    fun deletePlaylistForFinal() {
        if (deletePlaylistIndex != -1) {
            finalList.removeAt(deletePlaylistIndex)
            deletePlaylistIndex = -1
            viewModelScope.launch {
                _finalListFlow.emit(finalList)
            }
        }
    }

    fun createFinalModel() {
        finalModel?.run {
            val curFinal = Final(name, durationDance, durationPause, finalList)
            viewModelScope.launch(Dispatchers.IO) {
                repository.addFinal(curFinal)
            }
        }
    }
}