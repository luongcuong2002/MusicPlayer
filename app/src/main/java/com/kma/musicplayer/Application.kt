package com.kma.musicplayer

import android.app.Application
import com.kma.musicplayer.database.AppDatabase
import com.kma.musicplayer.utils.SharePrefUtils

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        SharePrefUtils.init(this)
        // Initialize the database
        AppDatabase.createInstance(this)
    }
}