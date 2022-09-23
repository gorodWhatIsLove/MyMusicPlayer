package com.example.mymusicplayer.presentation.model

import com.example.mymusicplayer.service.PlaybackStatus
import java.io.Serializable

data class SongPresentation(
    val id: String,
    val nameSong: String?,
    val artist: String?,
    val album: String?,
    val time: String,
    val duration: Long,
    val state: PlaybackStatus = PlaybackStatus.STOPED
) : Serializable
