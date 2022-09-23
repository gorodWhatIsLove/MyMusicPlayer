package com.example.mymusicplayer.di

import android.app.Application
import android.content.Context
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.data.MediaRepository
import com.example.mymusicplayer.data.MediaRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(val context: Context) {

//    @Singleton
//    @Provides
//    fun provideContext(): Context = app

    @Singleton
    @Provides
    fun provideRepositoryMedia(context: Context): MediaRepository =
        MediaRepositoryImpl(context)
}