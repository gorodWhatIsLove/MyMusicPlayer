package com.example.mymusicplayer

import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.example.mymusicplayer.data.model.SongEntity
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.model.SongPresentation
import com.example.mymusicplayer.service.PlaybackStatus
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

fun Long.toTime() : String {
    var time = ""
    val hrs = this / (1000*60*60)
    val min = (this%(1000*60*60))/(1000*60)
    val secs = (((this%(1000*60*60))%(1000*60*60))%(1000*60))/1000
    if(hrs < 1) {
        time = "$min:$secs"
    } else {
        time = "$hrs:$min:$secs"
    }
    return time
}

fun Song.toDao(): SongEntity = SongEntity(
    id = id,
    nameSong = nameSong,
    artist = artist,
    album = album,
    duration = duration,
    path = path
)

fun SongEntity.toDomain(): Song = Song(
    id = id,
    nameSong = nameSong,
    artist = artist,
    album = album,
    duration = duration,
    path = path
)

/**
 * Helper extension to convert a potentially null [String] to a [Uri] falling back to [Uri.EMPTY]
 */
fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY

fun CoroutineScope.launchPeriodicAsync(
    repeatMillis: Long,
    action: () -> Unit
) = this.async {
    if (repeatMillis > 0) {
        while (isActive) {
            action()
            delay(repeatMillis)
        }
    } else {
        action()
    }
}