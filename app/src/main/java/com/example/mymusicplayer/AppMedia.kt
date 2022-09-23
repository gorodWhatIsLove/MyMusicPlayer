package com.example.mymusicplayer

import android.app.Application
import com.example.mymusicplayer.di.ComponentMedia
import com.example.mymusicplayer.di.DaggerComponentMedia
import com.example.mymusicplayer.di.RepositoryModule
import com.example.mymusicplayer.di.ViewModelModule
import сore.viewmodel.ViewModelFactory

class AppMedia : Application() {

    val appComponent: ComponentMedia by lazy {
        DaggerComponentMedia
            .builder()
            .application(this)
            .bindContext(this)
            .repositoryModule(RepositoryModule(this))
//            .viewModelModule(ViewModelModule())
            .build()
    }
}