package com.kma.musicplayer.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.kma.musicplayer.model.FavouriteSong
import com.kma.musicplayer.model.HiddenSong

@Dao
interface HiddenSongDao {
    @Query("SELECT COUNT(*) FROM HiddenSong WHERE song_id = :songId")
    fun isHidden(songId: String): Boolean

    @Query("INSERT INTO HiddenSong (song_id) VALUES (:songId)")
    suspend fun insert(songId: String)

    suspend fun insertAll(songIds: List<String>) {
        songIds.forEach { insert(it) }
    }

    @Query("SELECT * FROM HiddenSong")
    fun getHiddenSongs(): List<HiddenSong>

    @Query("DELETE FROM HiddenSong WHERE song_id = :songId")
    suspend fun delete(songId: String)

    suspend fun deleteAll(songIds: List<String>) {
        songIds.forEach { delete(it) }
    }
}