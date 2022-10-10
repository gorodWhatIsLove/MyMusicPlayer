package com.example.mymusicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymusicplayer.domain.model.Song

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val listSongId: String?
    )