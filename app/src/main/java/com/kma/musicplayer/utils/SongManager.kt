package com.kma.musicplayer.utils

import android.util.Log
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.network.retrofit.model.SongDto
import com.kma.musicplayer.network.retrofit.repository.SongRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SongManager {

    val allSongs = mutableListOf<Song>()

    fun fetchAllOnlineSong(onSuccess: (List<OnlineSong>) -> Unit, onError: () -> Unit) {
        if (allSongs.isNotEmpty()) {
            onSuccess(allSongs.filterIsInstance<OnlineSong>())
            return
        }
        SongRepository.getAllSongs(object : Callback<List<SongDto>> {
            override fun onResponse(call: Call<List<SongDto>>, response: Response<List<SongDto>>) {
                val songs = response.body()?.map { it.toOnlineSong() } ?: emptyList()
                allSongs.addAll(songs)
                onSuccess(songs)
            }

            override fun onFailure(call: Call<List<SongDto>>, t: Throwable) {
                Log.d("SongManager", "onFailure: ${t.message}")
                onError()
            }
        })
    }

    fun getSongById(id: String): Song? {
        return allSongs.find { it.id == id }
        // must add get local song by id
    }
}