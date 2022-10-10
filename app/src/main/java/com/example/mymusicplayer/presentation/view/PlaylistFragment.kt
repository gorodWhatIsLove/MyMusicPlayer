package com.example.mymusicplayer.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentPlaylistBinding
import com.example.mymusicplayer.presentation.adapters.PlaylistAdapter
import com.example.mymusicplayer.presentation.view.viewmodel.AddSongToPlaylistViewModel
import com.example.mymusicplayer.presentation.view.viewmodel.PlaylistViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import сore.activityNavController
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private val binding by viewBinding(FragmentPlaylistBinding::bind)

    private val playlistAdapter = PlaylistAdapter {
        activityNavController().navigate(R.id.mainFlowFragment)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: PlaylistViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as? AppMedia)?.appComponent?.inject(this)
        viewModel.initPlaylist()
        binding.rvPlaylists.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = playlistAdapter
        }
        lifecycleScope.launch {
            viewModel.playlistFlow.collect {
                playlistAdapter.submitList(it)
                playlistAdapter.notifyDataSetChanged()
            }
        }
        binding.btnAddPlaylist.setOnClickListener {
            activityNavController().navigate(R.id.addSongToPlaylist)
        }
    }
}