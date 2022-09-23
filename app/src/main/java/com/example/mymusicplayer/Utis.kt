package com.example.mymusicplayer

import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.presentation.model.SongPresentation
import com.example.mymusicplayer.service.PlaybackStatus

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