package com.kma.musicplayer.utils

import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song

object SongManager {
    val allSongs = mutableListOf<Song>().apply {
        add(
            OnlineSong(
                "1",
                "Baby",
                "Justin Bieber",
                3 * 60 + 10,
                "https://images.pexels.com/photos/15636411/pexels-photo-15636411/free-photo-of-hoa-trang-tri-cu-c-s-ng-tinh-l-ng-nh.jpeg?auto=compress&cs=tinysrgb&w=600",
                "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Kangaroo_MusiQue_-_The_Neverwritten_Role_Playing_Game.mp3"
            )
        )
        add(
            OnlineSong(
                "2",
                "See you again",
                "Charlie Puth",
                5 * 60 + 12,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Kangaroo_MusiQue_-_The_Neverwritten_Role_Playing_Game.mp3"
            )
        )
        add(
            OnlineSong(
                "3",
                "Baby",
                "Justin Bieber",
                7 * 60 + 21,
                "https://i.pinimg.com/originals/11/de/d7/11ded77d12eed3ca0204ea387934aeaf.jpg",
                "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3"
            )
        )
        add(
            OnlineSong(
                "4",
                "See you again",
                "Charlie Puth",
                4 * 60 + 13,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3"
            )
        )
        add(
            OnlineSong(
                "5",
                "Baby",
                "Justin Bieber",
                3 * 60 + 10,
                "https://i.pinimg.com/originals/11/de/d7/11ded77d12eed3ca0204ea387934aeaf.jpg",
                "https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"
            )
        )
        add(
            OnlineSong(
                "6",
                "See you again",
                "Charlie Puth",
                3 * 60 + 10,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                "https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"
            )
        )
        add(
            OnlineSong(
                "7",
                "Baby",
                "Justin Bieber",
                2 * 60 + 46,
                "https://i.pinimg.com/originals/11/de/d7/11ded77d12eed3ca0204ea387934aeaf.jpg",
                "https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"
            )
        )
        add(
            OnlineSong(
                "8",
                "See you again",
                "Charlie Puth",
                1 * 60 + 65,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8qVLXZ511VApVjnOcB75QtyyTyO-D_3mDXA&s",
                "https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3"
            )
        )
    }

    fun getAllOnlineSong(): List<OnlineSong> {
        return allSongs.filterIsInstance<OnlineSong>()
    }

    fun getSongById(id: String): Song? {
        return allSongs.find { it.id == id }
    }
}