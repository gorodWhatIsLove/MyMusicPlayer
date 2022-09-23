package com.example.mymusicplayer.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.data.MediaRepositoryImpl
import com.example.mymusicplayer.databinding.FragmentMainBinding
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.MainActivity.Companion.PLAYER_RECEIVER
import com.example.mymusicplayer.presentation.adapters.SongAdapter
import dagger.android.support.DaggerFragment
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject


class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private lateinit var component: ComponentMedia

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }
//    private lateinit var viewModel: MainViewModel

    private var songAdapter: SongAdapter = SongAdapter() { song ->
        (parentFragment?.parentFragment as? MainFlowFragment)?.visItemPlayer()
        (activity as? MainActivity)?.playAudio(song.path)
        val broadcastIntent = Intent(PLAYER_RECEIVER)
        broadcastIntent.putExtra("SONG", song)
        activity?.sendBroadcast(broadcastIntent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component = (activity?.application as AppMedia).appComponent
        component.inject(this)
//        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvSongs.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }

        songAdapter.submitList(viewModel.getListSongs())
    }

    companion object {
        const val SONG_PATH = "SONG_PATH"
    }
}