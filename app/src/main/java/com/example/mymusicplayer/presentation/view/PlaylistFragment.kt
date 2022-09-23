package com.example.mymusicplayer.presentation.view

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentPlaylistBinding
import com.example.mymusicplayer.presentation.adapters.PlaylistAdapter

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private val binding by viewBinding(FragmentPlaylistBinding::bind)

    private val playlistAdapter = PlaylistAdapter {

    }
}