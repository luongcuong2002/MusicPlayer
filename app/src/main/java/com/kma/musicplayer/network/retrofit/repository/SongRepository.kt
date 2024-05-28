package com.kma.musicplayer.network.retrofit.repository

import com.kma.musicplayer.network.retrofit.RetrofitClient
import com.kma.musicplayer.network.retrofit.api.SongApi
import com.kma.musicplayer.network.retrofit.model.SongDto
import retrofit2.Callback

class SongRepository {
    private val songApi: SongApi = RetrofitClient.getClient().create(SongApi::class.java)

    fun getAllSongs(callback: Callback<List<SongDto>>) {
        songApi.getAllSongs().enqueue(callback)
    }
}