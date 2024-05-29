package com.kma.musicplayer.utils

import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.network.retrofit.model.SongDto

object Mapping {
    fun mapToOnlineSong(songDto: SongDto): OnlineSong {
        return OnlineSong(
            id = songDto.id,
            title = songDto.title,
            artist = songDto.artist,
            duration = songDto.duration,
            thumbnail = songDto.thumbnail,
            path = songDto.path
        )
    }
}