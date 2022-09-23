package com.example.mymusicplayer.service

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mymusicplayer.presentation.view.MainFragment.Companion.SONG_PATH

class PlayerWorker(context: Context, params: WorkerParameters) : Worker(context, params) {


    override fun doWork(): Result {
        try {
            val path = inputData.getString(SONG_PATH)
            path?.let {
                val mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(path)
                    prepare()
                    start()
                }
            }

            //Ваш код
        } catch (ex: Exception) {
            return Result.failure(); //или Result.retry()
        }
        return Result.success()
    }
}