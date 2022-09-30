package com.example.mymusicplayer

import android.app.Application
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.di.DaggerComponentMedia
import com.example.mymusicplayer.di.RepositoryModule
import com.example.mymusicplayer.di.ViewModelModule
import com.google.android.exoplayer2.ExoPlayer
import сore.viewmodel.ViewModelFactory

class AppMedia : Application() {

    val appComponent: ComponentMedia by lazy {
        DaggerComponentMedia
            .builder()
            .application(this)
            .bindContext(this)
            .repositoryModule(RepositoryModule(this))
//            .exoPlayerModule(ExoPlayer.Builder(this).build())
//            .viewModelModule(ViewModelModule())
            .build()
    }
}