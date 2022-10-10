package com.example.mymusicplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mymusicplayer.data.model.PlaylistEntity
import com.example.mymusicplayer.data.model.SongEntity

@Database(entities = [SongEntity::class, PlaylistEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
}