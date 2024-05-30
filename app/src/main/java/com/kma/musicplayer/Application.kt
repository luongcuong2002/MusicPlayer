package com.kma.musicplayer

import android.app.Application
import com.kma.musicplayer.database.AppDatabase

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the database
        AppDatabase.createInstance(this)
    }
}