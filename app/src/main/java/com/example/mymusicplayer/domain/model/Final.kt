package com.example.mymusicplayer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Final(
    val name: String,
    val durationDance: Int,
    val durationPause: Int,
    val listPlaylist: MutableList<String>
) : Parcelable
