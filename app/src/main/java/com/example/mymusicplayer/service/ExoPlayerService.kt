package com.example.mymusicplayer.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.net.toUri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class ExoPlayerService(val context: Context) :Service() {

    lateinit var exoPlayer: ExoPlayer

    // Binder given to clients
    private val iBinder: IBinder = LocalBinder()

    private var mediaFile: String? = null

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(context).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    override fun onBind(p0: Intent?): IBinder = iBinder

    fun initExoPlayer() {
        mediaFile?.let { file ->
            val mediaItem = MediaItem.fromUri(file.toUri())

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            play()
        }

    }

    fun play() {
        exoPlayer.play()
    }

    fun stop() {
        exoPlayer.stop()
    }

    fun next() {
        exoPlayer.seekToNext()
    }

    fun previous() {
        exoPlayer.seekToPrevious()
    }

    inner class LocalBinder : Binder() {
        val service: ExoPlayerService
            get() = this@ExoPlayerService
    }
}