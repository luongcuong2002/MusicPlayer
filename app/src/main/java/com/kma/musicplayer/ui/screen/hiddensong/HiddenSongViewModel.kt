package com.kma.musicplayer.ui.screen.hiddensong

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kma.musicplayer.model.OnlineSong
import com.kma.musicplayer.model.Song
import com.kma.musicplayer.ui.screen.songselector.SelectableSong

class HiddenSongViewModel : ViewModel() {
    val songs = mutableListOf<SelectableSong>()

    private val _isSelectAll = MutableLiveData(false)
    val isSelectAll: LiveData<Boolean> = _isSelectAll

    private val _isAtLeastOneSelected = MutableLiveData(false)
    val isAtLeastOneSelected: LiveData<Boolean> = _isAtLeastOneSelected

    private val _isListEmpty = MutableLiveData(false)
    val isListEmpty: LiveData<Boolean> = _isListEmpty

    fun setSongs(songs: List<Song>) {
        this.songs.clear()
        songs.forEach {
            this.songs.add(SelectableSong(it))
        }
        checkListEmpty()
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

    fun getSelectedSongs(): List<Song> {
        return songs.filter { it.isSelected }.map { it.song }
    }

    fun checkListEmpty() {
        _isListEmpty.value = songs.isEmpty()
    }
}