package com.kma.musicplayer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class HiddenSong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "song_id")
    val songId: String
) : Serializable