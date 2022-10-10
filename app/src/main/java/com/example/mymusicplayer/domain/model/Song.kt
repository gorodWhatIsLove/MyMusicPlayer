package com.example.mymusicplayer.domain.model

import java.io.Serializable


data class Song(
    val id: String,
    val nameSong: String?,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val path: String,
) : Serializable

enum class PlayerState {
    PLAY,
    STOP,
    PAUSE
}
