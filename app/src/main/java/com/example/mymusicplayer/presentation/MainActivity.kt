package com.example.mymusicplayer.presentation

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.R
import com.example.mymusicplayer.databinding.ActivityMainBinding
import com.example.mymusicplayer.service.MediaPlayerService
import com.example.mymusicplayer.service.MediaPlayerService.LocalBinder


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var mMediaBrowserCompat: MediaBrowserCompat

//    private var player: MediaPlayerService? = null
//    var serviceBound = false
    private val permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsResult ->
        if (permissionsResult.entries.find { !it.value } == null)  {
            verifyStoragePermissions()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as AppMedia).appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        if(!hasStorePermission()) {
            permissionsLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
        mMediaBrowserCompat = MediaBrowserCompat(
            this, componentName, //Identifier for the service
            connectionCallback,
            null
        )
    }

    override fun onStart() {
        super.onStart()
        mMediaBrowserCompat.connect()
    }

    private val connectionCallback: MediaBrowserCompat.ConnectionCallback =
        object : MediaBrowserCompat.ConnectionCallback() {
            override fun onConnected() {

                // The browser connected to the session successfully, use the token to create the controller
                super.onConnected()
                mMediaBrowserCompat.sessionToken.also { token ->
                    val mediaController = MediaControllerCompat(this@MainActivity, token)
                    MediaControllerCompat.setMediaController(this@MainActivity, mediaController)
                }
//                playPauseBuild()
                Log.d("WhatIsLove", "Controller Connected")
            }

            override fun onConnectionFailed() {
                super.onConnectionFailed()
                Log.d("WhatIsLove", "Connection Failed")

            }

        }
    private val mControllerCallback = object : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            Log.d("WhatIsLove", "Что-то поменялось $state")
        }
    }

    fun playPauseBuild(path: String) {
        val mediaController = MediaControllerCompat.getMediaController(this)
        val state = mediaController.playbackState?.state
        when(state) {
            // if it is not playing then what are you waiting for ? PLAY !
            PlaybackStateCompat.STATE_PAUSED,
            PlaybackStateCompat.STATE_STOPPED,
            PlaybackStateCompat.STATE_NONE -> {
                mediaController.transportControls.playFromUri(Uri.parse(path), null)
                Log.e("WhatISLove", mediaController.playbackState?.state.toString())
                val broadcastIntent = Intent(PLAYER_RECEIVER)
                broadcastIntent.putExtra("STATE_SONG", state)
                sendBroadcast(broadcastIntent)
            }
            // you are playing ? knock it off !
            PlaybackStateCompat.STATE_PLAYING,
            PlaybackStateCompat.STATE_BUFFERING,
            PlaybackStateCompat.STATE_CONNECTING -> {
                mediaController.transportControls.pause()
                Log.e("WhatISLove", mediaController.playbackState?.state.toString())
                val broadcastIntent = Intent(PLAYER_RECEIVER)
                broadcastIntent.putExtra("STATE_SONG", state)
                sendBroadcast(broadcastIntent)
            }
        }
        mediaController.registerCallback(mControllerCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        val controllerCompat = MediaControllerCompat.getMediaController(this)
        controllerCompat?.unregisterCallback(mControllerCallback)
        mMediaBrowserCompat.disconnect()
    }

    private fun verifyStoragePermissions(): Boolean {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun hasStorePermission(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(R.id.mainFlowFragment)
        navController.graph = navGraph
    }

    companion object {
        const val Broadcast_PLAY_NEW_AUDIO = "com.valdioveliu.valdio.audioplayer.PlayNewAudio"
        const val PLAYER_RECEIVER = "PlayerItemIntent"
        const val PERMISSION_REQUEST_CODE = 4532
    }
}