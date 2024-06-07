package com.kma.musicplayer.model

import com.kma.musicplayer.R

enum class SleepTimerModel(
    val time: Long,
    val textRes: Int
) {
    _5_MINUTES(5 * 60, R.string._5_minutes),
    _10_MINUTES(10 * 60, R.string._10_minutes),
    _15_MINUTES(15 * 60, R.string._15_minutes),
    _30_MINUTES(20 * 60, R.string._30_minutes),
    _40_MINUTES(45 * 60, R.string._40_minutes),
    END_OF_TRACK(0, R.string.end_of_track),
    CUSTOM(0, R.string.custom),
    TURN_OFF(0, R.string.turn_off)
}