package com.kma.musicplayer.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class CenterLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)
    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {

        val firstVisibleItemWhenViewFromCurrentPosition =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItemWhenViewFromCurrentPosition =
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        val millisecondsPerInch = if (position in firstVisibleItemWhenViewFromCurrentPosition..lastVisibleItemWhenViewFromCurrentPosition) {
            300f
        } else if (position < firstVisibleItemWhenViewFromCurrentPosition) {
            300f / (firstVisibleItemWhenViewFromCurrentPosition - position)
        } else {
            300f / (position - lastVisibleItemWhenViewFromCurrentPosition)
        }

        val centerSmoothScroller = CenterSmoothScroller(recyclerView.context, millisecondsPerInch)
        centerSmoothScroller.targetPosition = position
        startSmoothScroll(centerSmoothScroller)
    }

    private class CenterSmoothScroller(context: Context, val millisecondsPerInch: Float) : LinearSmoothScroller(context) {

        //private val MILLISECONDS_PER_INCH = 300f //default is 25f (bigger = slower)

        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int = (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            Log.d("DFDSDSDSDSD", "calculateSpeedPerPixel: ${displayMetrics?.widthPixels}")
            return if (displayMetrics != null) {
                millisecondsPerInch / displayMetrics.densityDpi
            } else {
                super.calculateSpeedPerPixel(displayMetrics)
            }
        }
    }
}