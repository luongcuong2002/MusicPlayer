package com.kma.musicplayer.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("thumbnail") val thumbnail: String
) : Serializable