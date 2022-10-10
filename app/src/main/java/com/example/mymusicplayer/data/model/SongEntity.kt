package com.example.mymusicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey
    val id: String,
    val nameSong: String?,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val path: String
)
