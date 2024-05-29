package com.kma.musicplayer.customview

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.kma.musicplayer.R
import com.kma.musicplayer.extension.findActivity

class BackButton : AppCompatImageView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setImageResource(R.drawable.ic_arrow_back)
        setOnClickListener {
            (context.findActivity())?.onBackPressed()
        }
    }
}