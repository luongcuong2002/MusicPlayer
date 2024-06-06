package com.kma.musicplayer.model

class OnlineSong(
    id: String,
    title: String,
    artist: Artist,
    duration: Int,
    val thumbnail: String,
    path: String
) : Song(id, title, artist, duration, path)