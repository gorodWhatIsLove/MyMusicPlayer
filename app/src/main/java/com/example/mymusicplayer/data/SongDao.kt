package com.example.mymusicplayer.data

import androidx.room.*
import com.example.mymusicplayer.data.model.SongEntity

@Dao
interface SongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongList(songList: List<SongEntity>)

    @Insert
    fun insertSong(song: SongEntity)

    @Delete
    fun delete(song: SongEntity)

//    @Query("SELECT * FROM song WHERE playlist IN (:playlist) ")
//    fun findByPlaylist(playlist: String): List<SongEntity>

    @Update
    fun updateSongList(songList: List<SongEntity>)

    @Query("SELECT * FROM song")
    fun getSongList(): List<SongEntity>
}