package com.kma.musicplayer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.utils.SharePrefUtils

class ThemeViewModel : ViewModel() {
    private var _theme: MutableLiveData<Theme> = MutableLiveData<Theme>(Theme.DARK)
    val theme: LiveData<Theme> = _theme

    fun setCurrentTheme(theme: Theme) {
        _theme.value = theme
        SharePrefUtils.saveTheme(theme)
    }
}
