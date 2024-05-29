package com.kma.musicplayer.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class SongDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("path") val path: String
)
