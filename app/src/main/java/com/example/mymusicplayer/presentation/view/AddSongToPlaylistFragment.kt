package com.example.mymusicplayer.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentAddSongToPlaylistBinding
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.adapters.AddSongAdapter
import com.example.mymusicplayer.presentation.view.viewmodel.AddSongToPlaylistViewModel
import com.example.mymusicplayer.presentation.view.viewmodel.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import сore.activityNavController
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject

class AddSongToPlaylistFragment : Fragment(R.layout.fragment_add_song_to_playlist) {

    private val binging by viewBinding(FragmentAddSongToPlaylistBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AddSongToPlaylistViewModel by viewModels {
        viewModelFactory
    }

    private val songAdapter: AddSongAdapter = AddSongAdapter { song: Song, isSelected: Boolean ->
        if(isSelected) {
            viewModel.setSong(song)
        } else {
            viewModel.removeSong(song)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as? AppMedia)?.appComponent?.inject(this)
        binging.rvSongs.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }
        lifecycleScope.launch {
            viewModel.songListFlow.collect{
                songAdapter.submitList(it)
            }
        }
        binging.btnCreatePlaylist.setOnClickListener {
            viewModel.setPlaylistToSong(binging.etNamePlaylist.text.toString())
            activityNavController().popBackStack()
        }
    }
}