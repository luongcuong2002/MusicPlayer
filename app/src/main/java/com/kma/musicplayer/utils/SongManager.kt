package com.kma.musicplayer.utils

import android.util.Log
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.network.retrofit.model.SongDto
import com.kma.musicplayer.network.retrofit.repository.SongRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

object SongManager {

    val allSongs = mutableListOf<Song>()

    fun fetchAllOnlineSong(onSuccess: ((List<OnlineSong>) -> Unit)? = null, onError: (() -> Unit)? = null) {
        if (allSongs.isNotEmpty()) {
            onSuccess?.invoke(allSongs.filterIsInstance<OnlineSong>())
            return
        }
        SongRepository.getAllSongs(object : Callback<List<SongDto>> {
            override fun onResponse(call: Call<List<SongDto>>, response: Response<List<SongDto>>) {
                val songs = response.body()?.map { it.toOnlineSong() } ?: emptyList()
                allSongs.addAll(songs)
                onSuccess?.invoke(songs.filter { !AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id) })
            }

            override fun onFailure(call: Call<List<SongDto>>, t: Throwable) {
                Log.d("SongManager", "onFailure: ${t.message}")
                onError?.invoke()
            }
        })
    }

    fun getSongById(id: String): Song? {
        val song = allSongs.find { it.id == id }
        song?.let {
            if (song.id.startsWith(Constant.LOCAL_AUDIO_PREFIX_ID) && File(song.path).exists().not()) {
                allSongs.remove(song)
                return null
            }
        }
        return song
    }

    fun getAllArtist(): List<Artist> {
        return allSongs.mapNotNull { it.artist }.distinct().filter { getSongByArtist(it).isNotEmpty() }
    }

    fun getSongByArtist(artist: Artist): List<Song> {
        return allSongs.filter { it.artist == artist }.filter { !AppDatabase.INSTANCE.hiddenSongDao().isHidden(it.id) }
    }
}