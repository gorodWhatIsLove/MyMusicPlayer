package com.example.mymusicplayer.presentation.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FlowFragmentMainBinding
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.MainActivity.Companion.PLAYER_RECEIVER
import com.example.mymusicplayer.presentation.base.BaseFlowFragment
import com.example.mymusicplayer.presentation.view.viewmodel.MainFlowViewModel
import kotlinx.coroutines.*
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainFlowFragment : BaseFlowFragment(
    R.layout.flow_fragment_main, R.id.nav_host_fragment_main
) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val binding by viewBinding(FlowFragmentMainBinding::bind)

    private var isConnectExoPlayer = false

    private val viewModel: MainFlowViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var component: ComponentMedia

    private val playerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            (intent?.getSerializableExtra("SONG") as? Song)?.let { song ->
                when {
                    viewModel.curSong == null -> {
                        viewModel.curSong = song
                    }
                    viewModel.curSong != song -> {
                        viewModel.curSong = song
                        viewModel.stopTimer()
                        Log.e("WhatIsLove", "Я создал таймер")
//                        viewModel.createTimer(viewModel.curSong?.duration ?: 0)
                        isConnectExoPlayer = false
                    }
                }
            }
            intent?.getIntExtra("STATE_SONG", -1)?.let { state ->
                if (state != -1) {
                    bindPlayer(state)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter(PLAYER_RECEIVER)
        activity?.registerReceiver(playerReceiver, intentFilter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component = (activity?.application as AppMedia).appComponent
        component.inject(this)
        binding.itemPlayer.tvNameSong.isSelected = true
        lifecycleScope.launch {
            viewModel.timerSong.collect {
                with(binding.itemPlayer) {
//                    sliderTime.value = it
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        with(binding.itemPlayer) {
            itemPlayer.setOnClickListener {
                goneItemPlayer()
                navController.navigate(R.id.playerFragment)
            }
            btnPlayOrPause.setOnClickListener {
                (activity as? MainActivity)?.playPauseBuild(viewModel.curSong?.path ?: "")
            }
        }
    }

    override fun setupNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun startStopPlaySong(state: Int) {
        with(binding.itemPlayer) {
            when (state) {
                PlaybackStateCompat.STATE_NONE -> {
                    if (!isConnectExoPlayer) {
                        Log.e("WhatIsLove", "И я создал таймер")
                        btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_pause))
                        viewModel.createTimer(viewModel.curSong?.duration ?: 0)
                        isConnectExoPlayer = true
                    } else {

                    }
                }
                PlaybackStateCompat.STATE_STOPPED -> {
                    viewModel.stopTimer()
                }
                PlaybackStateCompat.STATE_PAUSED -> {
                    Log.e("WhatIsLove", "Play")
                    btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_pause))
                    viewModel.startTimer()
                }
                PlaybackStateCompat.STATE_PLAYING,
                PlaybackStateCompat.STATE_BUFFERING,
                PlaybackStateCompat.STATE_CONNECTING -> {
                    Log.e("WhatIsLove", "Pause")
                    btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_play))
                    viewModel.pauseTimer()
                }
                else -> {
                    Log.e("WhatIsLove", "Ну это совсем пиздец")
                }
            }
        }
    }

    fun bindPlayer(state: Int) {
        with(binding.itemPlayer) {
            tvArtist.text = viewModel.curSong?.artist
            tvNameSong.text = viewModel.curSong?.nameSong
            startStopPlaySong(state)
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