package com.example.mymusicplayer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymusicplayer.data.model.FinalEntity
import com.example.mymusicplayer.data.model.PlaylistEntity

@Dao
interface FinalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserFinal(finalModel: FinalEntity)

    @Query("SELECT * FROM final")
    fun getFinal(): List<FinalEntity>
}