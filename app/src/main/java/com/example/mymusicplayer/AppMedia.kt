package com.example.mymusicplayer

import android.app.Application
import com.example.mymusicplayer.di.*
import com.google.android.exoplayer2.ExoPlayer
import сore.viewmodel.ViewModelFactory

class AppMedia : Application() {

    val appComponent: ComponentMedia by lazy {
        DaggerComponentMedia
            .builder()
            .application(this)
            .bindContext(this)
            .repositoryModule(RepositoryModule(this))
            .exoPlayerModule(ExoPlayerModule(this))
//            .viewModelModule(ViewModelModule())
            .build()
    }
}