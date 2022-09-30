package com.example.mymusicplayer.presentation.view

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.FragmentMainBinding
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.MainActivity.Companion.PLAYER_RECEIVER
import com.example.mymusicplayer.presentation.adapters.SongAdapter
import com.example.mymusicplayer.presentation.view.viewmodel.MainViewModel
import com.example.mymusicplayer.service.ExoPlayerService
import сore.viewmodel.ViewModelFactory
import javax.inject.Inject


class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    private lateinit var component: ComponentMedia

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mMediaBrowserCompat: MediaBrowserCompat
    private val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    private var songAdapter: SongAdapter = SongAdapter() { song ->
        (parentFragment?.parentFragment as? MainFlowFragment)?.visItemPlayer()
        val broadcastIntent = Intent(PLAYER_RECEIVER)
        broadcastIntent.putExtra("SONG", song)
        activity?.sendBroadcast(broadcastIntent)
        (activity as? MainActivity)?.playPauseBuild(song.path)
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserCompat.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val componentName = ComponentName(requireContext(), ExoPlayerService::class.java)
        // initialize the browser
        mMediaBrowserCompat = MediaBrowserCompat(
            requireContext(), componentName, //Identifier for the service
            connectionCallback,
            null
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component = (activity?.application as AppMedia).appComponent
        component.inject(this)
//        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        initRecycler()
    }

    override fun onStop() {
        super.onStop()
        val controllerCompat = activity?.let { MediaControllerCompat.getMediaController(it) }
        controllerCompat?.unregisterCallback(mControllerCallback)
        mMediaBrowserCompat.disconnect()
    }

    private val connectionCallback: MediaBrowserCompat.ConnectionCallback =
        object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {

                // The browser connected to the session successfully, use the token to create the controller
                super.onConnected()
                mMediaBrowserCompat.sessionToken.also { token ->
                    val mediaController = MediaControllerCompat(requireContext(), token)
                    activity?.let { MediaControllerCompat.setMediaController(it, mediaController) }
                }
//                playPauseBuild()
                Log.d("onConnected", "Controller Connected")
            }

            override fun onConnectionFailed() {
                super.onConnectionFailed()
                Log.d("onConnectionFailed", "Connection Failed")

            }

        }
    private val mControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)

        }
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