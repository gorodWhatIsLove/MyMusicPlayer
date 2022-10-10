package com.example.mymusicplayer.presentation.view

import android.media.session.PlaybackState
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentPlayerBinding
import com.example.mymusicplayer.databinding.FragmentPlaylistBinding
import com.example.mymusicplayer.launchPeriodicAsync
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.base.BaseFlowFragment
import com.example.mymusicplayer.toTime
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.Listener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import сore.activityNavController
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val binding by viewBinding(FragmentPlayerBinding::bind)

    @Inject
    lateinit var player: ExoPlayer

    private val playerListener = object : Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            with(binding) {
                bindPlayer()
            }
        }

        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            super.onMediaMetadataChanged(mediaMetadata)

        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when(playbackState) {
                PlaybackState.STATE_PLAYING -> {
                    binding.btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_pause))
                }
                PlaybackState.STATE_PAUSED -> {
                    binding.btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle))
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as AppMedia).appComponent.inject(this)
        playerControls()
        binding.btnBack.setOnClickListener {
            (parentFragment?.parentFragment as? MainFlowFragment)?.visItemPlayer()
            activityNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player.removeListener(playerListener)
    }

    private fun updatePlayerPositionProgress() {
        CoroutineScope(Dispatchers.IO).launchPeriodicAsync(1000) {
            if (player.isPlaying) {
                with(binding) {
                    sliderTime.progress = player.currentPosition.toInt()
                    timeCurrent.text = player.currentPosition.toTime()
                }
                updatePlayerPositionProgress()
            }
        }
    }

    private fun playerControls() {
        with(binding) {
            if (player.isPlaying) {
                btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_pause))
            } else {
                btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle))
            }
        }
        bindPlayer()
        setListenerButton()
        player.addListener(playerListener)
    }

    private fun setListenerButton() {
        with(binding) {
            btnPlayOrPause.setOnClickListener {
                if (player.isPlaying) {
                    btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_play_circle))
                    player.pause()
                } else {
                    btnPlayOrPause.setImageDrawable(resources.getDrawable(R.drawable.ic_media_pause))
                    player.play()
                }
            }
            btnNext.setOnClickListener {
                player.seekToNext()
            }
            btnPrev.setOnClickListener {
                player.seekToPrevious()
            }
        }
    }

    private fun bindPlayer() {
        with(player) {
            with(binding) {
                tvTitleSong.isSelected = true
                tvTitleSong.text =
                    "${currentMediaItem?.mediaMetadata?.artist} - ${currentMediaItem?.mediaMetadata?.title}"
                sliderTime.progress = currentPosition.toInt()
                sliderTime.max = duration.toInt()
                timeCurrent.text = currentPosition.toTime()
                timeEnd.text = duration.toTime()
            }
        }
        updatePlayerPositionProgress()
    }
}
