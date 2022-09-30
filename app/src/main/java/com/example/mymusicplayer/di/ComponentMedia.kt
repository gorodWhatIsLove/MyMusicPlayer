package com.example.mymusicplayer.di


import android.app.Application
import android.content.Context
import com.example.mymusicplayer.AppMedia
import com.example.mymusicplayer.presentation.MainActivity
import com.example.mymusicplayer.presentation.view.MainFlowFragment
import com.example.mymusicplayer.presentation.view.MainFragment
import com.google.android.exoplayer2.ExoPlayer
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import сore.viewmodel.ViewModelFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, ViewModelModule::class, ExoPlayerModule::class])
interface ComponentMedia {

    @Component.Builder
    interface Builder {
        fun build(): ComponentMedia

        @BindsInstance
        fun bindContext(context: Context): Builder
        @BindsInstance
        fun application(application: Application): Builder

        fun repositoryModule(repositoryModule: RepositoryModule): Builder

//        fun exoPlayerModule(exoPlayer: ExoPlayer): Builder

//        @BindsInstance
//        fun viewModelModule(viewModelModule: ViewModelModule): Builder
    }

    fun inject(activity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(mainFlowFragment: MainFlowFragment)
}