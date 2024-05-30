package com.kma.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HiddenSongDao {
    @Query("SELECT COUNT(*) FROM HiddenSong WHERE song_id = :songId")
    fun isHidden(songId: String): Boolean

    @Query("INSERT INTO HiddenSong (song_id) VALUES (:songId)")
    suspend fun insertHiddenSong(songId: String)

    suspend fun insertAll(songIds: List<String>) {
        songIds.forEach { insertHiddenSong(it) }
    }
}