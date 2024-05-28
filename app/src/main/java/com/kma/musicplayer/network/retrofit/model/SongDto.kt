package com.kma.musicplayer.network.retrofit.model

import com.google.gson.annotations.SerializedName

data class SongDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("singer") val singer: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("duration") val duration: Int,
)
