package com.example.mymusicplayer.di

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExoPlayerModule(val context: Context) {
    @Singleton
    @Provides
    fun provideExoPlayer(): ExoPlayer = ExoPlayer.Builder(context).build()
}