package com.kma.musicplayer.ui.customview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MovableText : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        this.ellipsize = TextUtils.TruncateAt.MARQUEE
        this.isSingleLine = true
        this.marqueeRepeatLimit = -1
        this.isSelected = true
    }
}
