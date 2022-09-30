package com.example.mymusicplayer.presentation.view.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.mymusicplayer.domain.model.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import сore.viewmodel.BaseViewModel
import javax.inject.Inject

class MainFlowViewModel @Inject constructor() : BaseViewModel() {

    private var playerTimer: Job? = null

    var curSong: Song? = null

    private val _timerSong: MutableSharedFlow<Float> = MutableSharedFlow()
    val timerSong: SharedFlow<Float> = _timerSong

    fun createTimer(duration: Long) {
        var time = 0F
        playerTimer = null
        playerTimer = viewModelScope.launch(Dispatchers.IO) {
            while (time < 100) {
                Log.e("timer", time.toString())
                _timerSong.emit(time)
                time++
                delay(duration / 100)
            }
        }
    }

    fun startTimer() {
        playerTimer?.start()
        Log.e("timer", "Timer start after pause")
    }

    fun pauseTimer() {
        playerTimer?.cancel()
        Log.e("timer", "Timer pause")
    }

    fun stopTimer() {
        playerTimer = null
        Log.e("timer", "Timer stop")
    }
}