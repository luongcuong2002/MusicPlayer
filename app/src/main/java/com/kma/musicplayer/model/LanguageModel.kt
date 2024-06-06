package com.kma.musicplayer.model

data class LanguageModel(
    var languageName: String,
    var isoLanguage: String,
    var isCheck: Boolean,
    var image: Int? = null
){
    constructor() : this("", "", false, 0) {}
}

