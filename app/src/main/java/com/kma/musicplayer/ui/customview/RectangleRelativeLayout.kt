package com.kma.musicplayer.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.kma.musicplayer.R

class RectangleRelativeLayout : RelativeLayout {

    private var _aspectRatio = 1F
    private var _dependOnDimension = 0

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RectangleRelativeLayout)
        _aspectRatio = typedArray.getFloat(R.styleable.RectangleRelativeLayout_rectAspectRatio, 1F)
        _dependOnDimension = typedArray.getInt(R.styleable.RectangleRelativeLayout_dependOnDimension, 0)
        typedArray.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RectangleRelativeLayout)
        _aspectRatio = typedArray.getFloat(R.styleable.RectangleRelativeLayout_rectAspectRatio, 1F)
        _dependOnDimension = typedArray.getInt(R.styleable.RectangleRelativeLayout_dependOnDimension, 0)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        // If dependOnDimension is 0, then the width is the dependent dimension
        if (_dependOnDimension == 0) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = (width / _aspectRatio).toInt()
            super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
            return
        }

        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = (height * _aspectRatio).toInt()
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }
}