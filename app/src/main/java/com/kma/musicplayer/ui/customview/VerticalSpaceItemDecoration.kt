package com.kma.musicplayer.ui.customview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if ((parent.getChildAdapterPosition(view) != (parent.adapter?.itemCount ?: 0) - 1)) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}