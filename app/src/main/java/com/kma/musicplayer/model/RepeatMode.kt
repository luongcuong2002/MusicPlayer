package com.kma.musicplayer.model

fun RepeatMode.nextMode(): RepeatMode {
    return when (this) {
        RepeatMode.NONE -> RepeatMode.REPEAT_ONE
        RepeatMode.REPEAT_ONE -> RepeatMode.REPEAT_ALL
        RepeatMode.REPEAT_ALL -> RepeatMode.NONE
    }
}

enum class RepeatMode {
    NONE,
    REPEAT_ALL,
    REPEAT_ONE;
}