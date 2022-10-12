package com.example.mymusicplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "final")
data class FinalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val durationDance: Int,
    val durationPause: Int,
    val PlaylistName: String
)
