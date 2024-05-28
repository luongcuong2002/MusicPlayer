package com.kma.musicplayer.network.retrofit.api

import com.kma.musicplayer.network.retrofit.model.SongDto
import retrofit2.Call
import retrofit2.http.GET

interface SongApi {
    @GET("get-all-songs")
    fun getAllSongs(): Call<List<SongDto>>
}