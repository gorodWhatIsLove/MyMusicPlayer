package com.example.mymusicplayer.presentation.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.session.PlaybackState
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FlowFragmentMainBinding
import com.example.mymusicplayer.domain.model.PlayerState
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.MainActivity.Companion.PLAYER_RECEIVER
import com.example.mymusicplayer.presentation.base.BaseFlowFragment
import com.example.mymusicplayer.presentation.model.SongPresentation
import com.example.mymusicplayer.service.PlaybackStatus
import com.example.mymusicplayer.toPresentation
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main, R.id.nav_host_fragment_main
) {

    private val binding by viewBinding(FlowFragmentMainBinding::bind)

    private val playerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            (intent?.getSerializableExtra("SONG") as? Song)?.let { song ->
                if(curSong == null) {
                    statusSong = PlaybackStatus.PLAYING
                    curSong = song
                } else {
                    if(curSong == song) {
                        changeStatusSong()
                    } else {
                        binding.itemPlayer.progressTime.progress = 0
                        statusSong = PlaybackStatus.PLAYING
                        curSong = song
                    }
                }
                bindPlayer()
            }
        }
    }

    private var curSong: Song? = null
    private var statusSong: PlaybackStatus = PlaybackStatus.STOPED
    private lateinit var playerTimer: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter(PLAYER_RECEIVER)
        activity?.registerReceiver(playerReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        binding.itemPlayer.btnPlayOrPause.setOnClickListener {
            (activity as? MainActivity)?.playAudio(curSong?.path ?: "")
            val broadcastIntent = Intent(PLAYER_RECEIVER)
            broadcastIntent.putExtra("SONG", curSong)
            activity?.sendBroadcast(broadcastIntent)
        }
    }

    override fun setupNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private suspend fun createTimer(duration: Long) = coroutineScope {
        playerTimer = launch {
            with(binding.itemPlayer) {
                while (progressTime.progress < 100) {
                    progressTime.progress += 1
                    delay(duration / 100)
                }
                statusSong = PlaybackStatus.STOPED
            }
        }
    }

    private suspend fun startTimer() {
        playerTimer.start()
    }

    private suspend fun stopTimer() {
        playerTimer.cancel()
    }

    private fun startStopPlaySong() {
        lifecycleScope.launch {
            with(binding.itemPlayer) {
                when (statusSong) {
                    PlaybackStatus.PLAYING -> {
                        btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_pause))
                        createTimer(curSong?.duration ?: 0)
                    }
                    PlaybackStatus.STOPED -> {
                        btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_play))
                        progressTime.progress = 0
                        stopTimer()
                    }
                    PlaybackStatus.PAUSED -> {
                        startTimer()
                    }
                }
            }
        }
    }

    private fun changeStatusSong() {
        when(statusSong) {
            PlaybackStatus.PLAYING -> {
                statusSong = PlaybackStatus.STOPED
            }
            PlaybackStatus.STOPED -> {
                binding.itemPlayer.progressTime.progress = 0
                statusSong = PlaybackStatus.PLAYING
            }
            PlaybackStatus.PAUSED -> {}
        }
    }

    fun bindPlayer() {
        with(binding.itemPlayer) {
            tvArtist.text = curSong?.artist
            tvNameSong.text = curSong?.nameSong
            startStopPlaySong()
        }
    }

    fun visItemPlayer() {
        if (!binding.itemPlayer.itemPlayer.isVisible) {
            binding.itemPlayer.itemPlayer.visibility = View.VISIBLE
        }
    }

    fun goneItemPlayer() {
        binding.itemPlayer.itemPlayer.visibility = View.GONE
    }
}