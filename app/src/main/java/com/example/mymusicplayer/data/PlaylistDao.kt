package com.example.mymusicplayer.data

import androidx.room.*
import com.example.mymusicplayer.data.model.PlaylistEntity
import com.example.mymusicplayer.data.model.SongEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: PlaylistEntity)

    @Delete
    fun delete(playlist: PlaylistEntity)

    @Query("SELECT * FROM song WHERE id IN (:id)")
    fun getSongById(id: String): SongEntity

    @Query("SELECT * FROM playlist")
    fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist WHERE name IN (:name)")
    fun getPlaylistByName(name: String): PlaylistEntity

}