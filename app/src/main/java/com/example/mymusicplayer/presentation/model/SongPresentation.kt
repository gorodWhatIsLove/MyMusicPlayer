package com.example.mymusicplayer.presentation.model

import android.content.Context
import com.example.mymusicplayer.R
import com.example.mymusicplayer.service.PlaybackStatus
import java.io.Serializable

data class SongPresentation(
    val id: String,
    val nameSong: String?,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val state: PlaybackStatus = PlaybackStatus.STOPED
) : Serializable {
    companion object {
        /**
         * Utility method to convert milliseconds to a display of minutes and seconds
         */
        fun timestampToMSS(context: Context, position: Long): String {
            val totalSeconds = Math.floor(position / 1E3).toInt()
            val minutes = totalSeconds / 60
            val remainingSeconds = totalSeconds - (minutes * 60)
            return if (position < 0) context.getString(R.string.duration_unknown)
            else context.getString(R.string.duration_format).format(minutes, remainingSeconds)
        }
    }
}