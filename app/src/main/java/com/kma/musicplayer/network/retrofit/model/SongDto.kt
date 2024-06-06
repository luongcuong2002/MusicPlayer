package com.kma.musicplayer.network.retrofit.model

import com.google.gson.annotations.SerializedName
import com.kma.musicplayer.model.Artist
import com.kma.musicplayer.model.OnlineSong

data class SongDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("duration") val duration: Int,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("path") val path: String
) {
    fun toOnlineSong(): OnlineSong {
        return OnlineSong(
            id = id,
            title = title,
            artist = artist,
            duration = duration,
            thumbnail = thumbnail,
            path = path
        )
    }
}
