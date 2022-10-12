package com.example.mymusicplayer.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymusicplayer.presentation.view.viewmodel.*
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import сore.viewmodel.ViewModelFactory
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun mainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainFlowViewModel::class)
    abstract fun mainFlowViewModel(viewModel: MainFlowViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistViewModel::class)
    abstract fun playlistViewModel(viewModel: PlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddSongToPlaylistViewModel::class)
    abstract fun addSongToPlaylistViewModel(viewModel: AddSongToPlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateFinalViewModel::class)
    abstract fun createFinalViewModel(viewModel: CreateFinalViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChoisePlaylistForFinalViewModel::class)
    abstract fun choisePlaylistForFinalViewModel(viewModel: ChoisePlaylistForFinalViewModel): ViewModel
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)