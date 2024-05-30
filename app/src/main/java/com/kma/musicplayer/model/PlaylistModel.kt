package com.kma.musicplayer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kma.musicplayer.database.converter.DataConverter
import java.io.Serializable

@Entity
data class PlaylistModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "playlist_name")
    val playlistName: String,

    @TypeConverters(DataConverter::class)
    @ColumnInfo(name = "song_ids")
    val songIds: MutableList<String> = mutableListOf(),
) : Serializable
