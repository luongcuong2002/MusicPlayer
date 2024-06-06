package com.kma.musicplayer.model

import java.io.Serializable

open class Song(
    val id: String,
    val title: String,
    val artist: Artist,
    val duration: Int,
    val path: String
) : Serializable
