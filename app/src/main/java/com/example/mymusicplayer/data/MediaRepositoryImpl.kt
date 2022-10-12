package com.example.mymusicplayer.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mymusicplayer.data.model.FinalEntity
import com.example.mymusicplayer.data.model.PlaylistEntity
import com.example.mymusicplayer.domain.model.Final
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.toDao
import com.example.mymusicplayer.toDomain
import com.example.mymusicplayer.toTime
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val context: Context,
    private val db: AppDatabase
) :
    MediaRepository {

    private val songList = mutableListOf<Song>()

    private val songDao = db.songDao()

    @SuppressLint("Range")
    override fun initMedia() {
        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }


        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"


        val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, selection)

        cursor?.let {
            if (cursor.moveToFirst()) {
                do {
                    songList.add(
                        Song(
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                        )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        songDao.insertSongList(songList.map { it.toDao() })
    }

    @SuppressLint("Range")
    override fun getSongs(): List<Song> = songList
    override fun addSongsPlaylist(namePlaylist: String, songs: List<Song>) {
        songs.forEach { song ->
            db.playlistDao().insert(PlaylistEntity(id = song.id, name = namePlaylist, listSongId = song.nameSong))
        }
    }

    override fun getPlaylists(): Set<String> {
        return db.playlistDao().getPlaylists().map { it.name }.toSet()
    }

    override fun addFinal(finalModel: Final) {
        finalModel.listPlaylist.forEach {
            db.finalDao()
                .inserFinal(FinalEntity(0, finalModel.name, finalModel.durationDance, finalModel.durationPause, it))
        }
    }

    override fun getFinal(): List<Final> {
        val finalList: MutableList<Final> = mutableListOf()
        db.finalDao().getFinal().forEach { final ->
            if(finalList.any { it.name == final.name }) {
                finalList.filter { it.name == final.name }.map {
                    it.listPlaylist.add(final.PlaylistName)
                }
            } else {
                finalList.add(Final(final.name, final.durationDance, final.durationPause, mutableListOf(final.PlaylistName)))
            }
        }
        return finalList
    }
}