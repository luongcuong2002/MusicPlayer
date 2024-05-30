package com.kma.musicplayer.model

import java.io.Serializable

data class OnlineSong(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Int,
    val thumbnail: String,
    val path: String
) : Serializable