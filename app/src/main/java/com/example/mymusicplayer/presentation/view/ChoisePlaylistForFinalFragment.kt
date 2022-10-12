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
import com.example.mymusicplayer.databinding.FragmentChoisePlaylistForFinalBinding
import com.example.mymusicplayer.domain.model.Final
import com.example.mymusicplayer.presentation.adapters.FinalAdapter
import com.example.mymusicplayer.presentation.adapters.PlaylistAdapter
import com.example.mymusicplayer.presentation.adapters.PlaylistFinalAdapter
import com.example.mymusicplayer.presentation.view.viewmodel.ChoisePlaylistForFinalViewModel
import com.example.mymusicplayer.presentation.view.viewmodel.CreateFinalViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import сore.activityNavController
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject

class ChoisePlaylistForFinalFragment : Fragment(R.layout.fragment_choise_playlist_for_final){

    private val binding by viewBinding(FragmentChoisePlaylistForFinalBinding::bind)

    private val playlistAdapter = PlaylistFinalAdapter { namePlaylist, isSelected ->
        if (isSelected) {
            viewModel.addFinalList.add(namePlaylist)
        } else {
            viewModel.addFinalList.remove(namePlaylist)
        }
    }

    private val finalAdapter = FinalAdapter {
        viewModel.deletePlaylistIndex = it
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ChoisePlaylistForFinalViewModel by viewModels {
        viewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as? AppMedia)?.appComponent?.inject(this)
        viewModel.finalModel = arguments?.getParcelable("FINAL_MODEL")
        initRecycler()
        initListener()
        lifecycleScope.launch{
            viewModel.finalListFlow.collect{
                finalAdapter.submitList(it)
                finalAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initRecycler() {
        binding.rvPlaylists.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = playlistAdapter
        }
        playlistAdapter.submitList(viewModel.finalModel?.listPlaylist)
        binding.rvFinal.apply {
            adapter = finalAdapter
        }
    }

    private fun initListener() {
        with(binding) {
            btnAddDance.setOnClickListener {
                viewModel.addPlaylistForFinal()
                playlistAdapter.submitList(viewModel.finalModel?.listPlaylist)
            }
            btnDeleteDance.setOnClickListener {
                viewModel.deletePlaylistForFinal()
                playlistAdapter.submitList(viewModel.finalModel?.listPlaylist)
                playlistAdapter.notifyDataSetChanged()
            }
            btnCreateFinal.setOnClickListener {
                viewModel.createFinalModel()
                activityNavController().popBackStack(R.id.navigation_home, false)
            }
        }
    }
}