package com.kma.musicplayer.model

class OnlineSong(
    id: String,
    title: String,
    val artist: String,
    duration: Int,
    val thumbnail: String,
    path: String
) : Song(id, title, duration, path)