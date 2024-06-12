package com.kma.musicplayer.utils

import android.content.Context
import android.content.SharedPreferences
import com.kma.musicplayer.model.Theme

object SharePrefUtils {
    private var mSharePref: SharedPreferences? = null
    fun init(context: Context) {
        if (mSharePref == null) {
            mSharePref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        }
    }

    fun saveSongIds(songIds: List<String>): Boolean {
        val editor = mSharePref!!.edit()
        editor.putString("songIds", songIds.joinTo(StringBuilder(), ",").toString())
        return editor.commit()
    }

    fun getSongIds(): List<String>? {
        val songIds = mSharePref!!.getString("songIds", "")
        if (songIds.isNullOrEmpty()) {
            return null
        }
        return songIds.split(",")
    }

    fun saveCurrentSongIndex(index: Int): Boolean {
        val editor = mSharePref!!.edit()
        editor.putInt("currentSongIndex", index)
        return editor.commit()
    }

    fun getCurrentSongIndex(): Int {
        return mSharePref!!.getInt("currentSongIndex", -1)
    }

    fun saveTheme(theme: Theme) {
        val editor = mSharePref!!.edit()
        editor.putString("themeName", theme.name)
        editor.commit()
    }

    fun getTheme(): Theme {
        val themeName = mSharePref!!.getString("themeName", Theme.DARK.name)
        return Theme.valueOf(themeName!!)
    }
}