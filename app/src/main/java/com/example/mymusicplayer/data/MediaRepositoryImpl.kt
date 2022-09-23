package com.example.mymusicplayer.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.mymusicplayer.domain.model.Song
import com.example.mymusicplayer.toTime
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(private val context: Context) : MediaRepository {

    private val songList = mutableListOf<Song>()

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
            if(cursor.moveToFirst()) {
                do {
                    songList.add(Song(
                        "1",
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)).toTime(),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    ))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
    }

    @SuppressLint("Range")
    override fun getSongs(): List<Song> = songList
}