package com.example.mymusicplayer

import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.model.SongPresentation
import com.example.mymusicplayer.service.PlaybackStatus
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes

fun Long.toTime() : String {
    val mil = this / 1000
    val sec = (mil % 60)
    val hour = mil / 360
    val minute = mil / 60 - hour * 60
    return String.format("$hour:$minute:$sec")
}

fun Song.toPresentation(state: PlaybackStatus) = SongPresentation(
    id = id,
    nameSong = nameSong,
    artist = artist,
    album = album,
    time = time,
    duration = duration,
    state = state
)

/**
 * Helper extension to convert a potentially null [String] to a [Uri] falling back to [Uri.EMPTY]
 */
fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY