package com.example.mymusicplayer.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentListSongBinding
import com.example.mymusicplayer.databinding.FragmentMainBinding
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.MainActivity.Companion.PLAYER_RECEIVER
import com.example.mymusicplayer.presentation.adapters.FinalAdapter
import com.example.mymusicplayer.presentation.adapters.SongAdapter
import com.example.mymusicplayer.presentation.view.viewmodel.MainViewModel
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import сore.activityNavController
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject


class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private lateinit var component: ComponentMedia

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

//    @Inject
//    lateinit var player: ExoPlayer

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private val finalAdapter: FinalAdapter = FinalAdapter {

    }

//    private val songAdapter: SongAdapter by lazy {
//        SongAdapter(player) { song ->
//            (parentFragment?.parentFragment as? MainFlowFragment)?.visItemPlayer()
//            val broadcastIntent = Intent(PLAYER_RECEIVER)
//            broadcastIntent.putExtra("SONG", song)
//            activity?.sendBroadcast(broadcastIntent)
//            (activity as? MainActivity)?.playPauseBuild(song.path)
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component = (activity?.application as AppMedia).appComponent
        component.inject(this)
        binding.btnCreateFinal.setOnClickListener {
            activityNavController().navigate(R.id.createFinalfragment)
        }
        viewModel.getListSongs()
        initRecycler()
        lifecycleScope.launch {
            viewModel.finalFlow.collect{
                finalAdapter.submitList(it)
            }
        }
    }

    private fun initRecycler() {
        binding.rvFinal.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = finalAdapter
        }
        //finalAdapter.submitList(viewModel.getFinalsName())
//        binding.rvSongs.apply {
//            layoutManager = LinearLayoutManager(this.context)
//            adapter = songAdapter
//        }
//
//        songAdapter.submitList(viewModel.getListSongs())
    }

    companion object {
        const val SONG_PATH = "SONG_PATH"
    }
}