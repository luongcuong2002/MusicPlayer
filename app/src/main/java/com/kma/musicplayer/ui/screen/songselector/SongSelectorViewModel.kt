package com.kma.musicplayer.ui.screen.songselector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.model.OnlineSong

class SongSelectorViewModel : ViewModel() {
    val songs = mutableListOf<SelectableSong>()

    private val _isSelectAll = MutableLiveData(false)
    val isSelectAll: LiveData<Boolean> = _isSelectAll

    private val _isAtLeastOneSelected = MutableLiveData(false)
    val isAtLeastOneSelected: LiveData<Boolean> = _isAtLeastOneSelected

    fun setSongs(songs: List<OnlineSong>) {
        this.songs.clear()
        songs.forEach {
            this.songs.add(SelectableSong(it))
        }
    }

    fun selectAll() {
        songs.forEach {
            it.isSelected = true
        }
        _isSelectAll.value = true
        _isAtLeastOneSelected.value = true
    }

    fun deselectAll() {
        songs.forEach {
            it.isSelected = false
        }
        _isSelectAll.value = false
        _isAtLeastOneSelected.value = false
    }

    fun checkSelectAll() {
        _isSelectAll.value = songs.all { it.isSelected }
    }

    fun checkAtLeastOneSelected() {
        _isAtLeastOneSelected.value = songs.any { it.isSelected }
    }

    fun getSelectedSongIds(): List<String> {
        return songs.filter { it.isSelected }.map { it.song.id }
    }
}