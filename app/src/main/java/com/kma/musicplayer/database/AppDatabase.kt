package com.kma.musicplayer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kma.musicplayer.database.converter.DataConverter
import com.kma.musicplayer.database.dao.HiddenSongDao
import com.kma.musicplayer.database.dao.PlaylistDao
import com.kma.musicplayer.model.HiddenSong
import com.kma.musicplayer.model.PlaylistModel

@TypeConverters(DataConverter::class)
@Database(entities = [PlaylistModel::class, HiddenSong::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao
    abstract fun hiddenSongDao(): HiddenSongDao

    companion object {
        lateinit var INSTANCE: AppDatabase
        fun createInstance(context: Context) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AppDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
        }
    }
}