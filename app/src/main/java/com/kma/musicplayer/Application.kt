package com.kma.musicplayer

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.model.Theme
import com.kma.musicplayer.utils.SharePrefUtils
import com.kma.musicplayer.viewmodel.ThemeViewModel

class Application : Application() {

    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate() {
        super.onCreate()
        SharePrefUtils.init(this)
        // Initialize the database
        AppDatabase.createInstance(this)

        createThemeViewModel()
    }

    private fun createThemeViewModel() {
        themeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(ThemeViewModel::class.java)
        themeViewModel.setCurrentTheme(SharePrefUtils.getTheme())
    }

    fun getThemeViewModel(): ThemeViewModel {
        return themeViewModel
    }
}