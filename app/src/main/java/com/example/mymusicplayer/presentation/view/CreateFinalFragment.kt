package com.example.mymusicplayer.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentCreateFinalBinding
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.adapters.PlaylistAdapter
import com.example.mymusicplayer.presentation.adapters.PlaylistFinalAdapter
import com.example.mymusicplayer.presentation.view.viewmodel.CreateFinalViewModel
import com.google.android.exoplayer2.Player
import com.google.android.material.internal.TextWatcherAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import сore.activityNavController
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject

class CreateFinalFragment : Fragment(R.layout.fragment_create_final) {

    val binding by viewBinding(FragmentCreateFinalBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val playlistAdapter = PlaylistFinalAdapter { namePlaylist: String, isSelected: Boolean ->
        if (isSelected) {
            viewModel.playlistList.add(namePlaylist)
        } else {
            viewModel.playlistList.remove(namePlaylist)
        }
    }

    private val viewModel: CreateFinalViewModel by viewModels {
        viewModelFactory
    }

    private val textWatcherNameFinal: TextWatcher = object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            viewModel.nameFinal = s.toString()
        }
    }

    private val textWatcherDance: TextWatcher = object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            viewModel.durationDance = s.toString()
        }
    }

    private val textWatcherPause: TextWatcher = object : TextWatcherAdapter() {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            viewModel.durationPause = s.toString()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as? AppMedia)?.appComponent?.inject(this)
        initRecycler()
        initListener()
        lifecycleScope.launch {
            viewModel.counterDanceFlow.collect(::changeCounterDance)
        }
        lifecycleScope.launch {
            viewModel.playlistFlow.collect {
                playlistAdapter.submitList(it)
            }
        }
    }

    private fun initRecycler() {
        binding.rvPlaylists.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = playlistAdapter
        }
    }

    private fun initListener() {
        with(binding) {
            btnMinus.setOnClickListener {
                if (viewModel.counterDance > 0) {
                    viewModel.counterDance--
                    viewModel.changeCountDance()
                }
            }
            btnPlus.setOnClickListener {
                viewModel.counterDance++
                viewModel.changeCountDance()
            }
            etNameFinal.addTextChangedListener(textWatcherNameFinal)
            etDurationDance.addTextChangedListener(textWatcherDance)
            etDurationPause.addTextChangedListener(textWatcherPause)
            btnCreateFinal.setOnClickListener {
                val final = viewModel.createFinalModel()
                var bundle = bundleOf("FINAL_MODEL" to final)
                activityNavController().navigate(R.id.choisePlaylistForFinalFragment, bundle)
            }
        }
    }

    private fun changeCounterDance(counterDance: Int) {
        binding.tvCounter.text = counterDance.toString()
    }
}