package com.kma.musicplayer.ui.customview

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.RelativeLayout
import androidx.core.view.marginRight
import com.kma.musicplayer.R

class CustomSwitch : RelativeLayout {

    private lateinit var view: View
    private lateinit var track: View
    private lateinit var thumb: View
    private lateinit var thumbContainer: View
    private lateinit var trackContainer: View

    private var checked: Boolean = true
    private var enable: Boolean = true
    private var invokeDefaultOnClick = true

    private var trackActiveResourceId: Int = R.drawable.track_selector
    private var trackInactiveResourceId: Int = R.drawable.track_selector_inactive
    private var thumbActiveResourceId: Int = R.drawable.thumb_selector
    private var thumbInactiveResourceId: Int = R.drawable.thumb_selector_inactive

    private var onCheckedChangeListener: OnCheckedChangeListener = OnCheckedChangeListener { _, b ->

    }

    private var onClick: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomSwitch, 0, 0
        )
        checked = a.getBoolean(R.styleable.CustomSwitch_checked, checked)
        invokeDefaultOnClick = a.getBoolean(R.styleable.CustomSwitch_invokeDefaultOnClick, invokeDefaultOnClick)
        trackActiveResourceId =
            a.getResourceId(R.styleable.CustomSwitch_trackActive, R.drawable.track_selector)
        trackInactiveResourceId = a.getResourceId(
            R.styleable.CustomSwitch_trackInactive,
            R.drawable.track_selector_inactive
        )
        thumbActiveResourceId =
            a.getResourceId(R.styleable.CustomSwitch_thumbActive, R.drawable.thumb_selector)
        thumbInactiveResourceId = a.getResourceId(
            R.styleable.CustomSwitch_thumbInactive,
            R.drawable.thumb_selector_inactive
        )
        a.recycle()

        init()
    }

    private fun init() {
        view = inflate(context, R.layout.custom_switch, this)
        thumb = view.findViewById(R.id.thumb)
        track = view.findViewById(R.id.track)
        thumbContainer = view.findViewById(R.id.thumbContainer)
        trackContainer = view.findViewById(R.id.trackContainer)

        track.post {
            handleWhenChangingChecked(doAnimation = false)
        }

        view.setOnClickListener {

            onClick?.let { it1 -> it1() }

            if (!invokeDefaultOnClick) {
                return@setOnClickListener
            }

            if (!enable) {
                return@setOnClickListener
            }
            checked = !checked
            handleWhenChangingChecked()
        }
    }

    private fun handleWhenChangingChecked(doAnimation: Boolean = true) {
        if (checked) {
            track.setBackgroundResource(trackActiveResourceId)
            thumb.setBackgroundResource(thumbActiveResourceId)

            if (!doAnimation) {
                thumbContainer.translationX = 0f
                return
            }

            ValueAnimator.ofFloat(thumbContainer.translationX, 0f).apply {
                addUpdateListener {
                    thumbContainer.translationX = it.animatedValue as Float
                }
                duration = 150
                start()
            }
        } else {
            track.setBackgroundResource(trackInactiveResourceId)
            thumb.setBackgroundResource(thumbInactiveResourceId)

            val endX =
                (-(trackContainer.width - 2 * thumbContainer.marginRight - thumbContainer.width)).toFloat()

            if (!doAnimation) {
                thumbContainer.translationX = endX
                return
            }

            ValueAnimator.ofFloat(0f, endX).apply {
                addUpdateListener {
                    thumbContainer.translationX = it.animatedValue as Float
                }
                duration = 150
                start()
            }
        }
        onCheckedChangeListener.onCheckedChanged(null, checked)
    }

    fun setOnClickListener(onClick: () -> Unit) {
        this.onClick = onClick
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        this.onCheckedChangeListener = listener
    }

    fun setChecked(value: Boolean, doAnimation: Boolean = true) {
        if (checked != value) {
            checked = value
            handleWhenChangingChecked(doAnimation)
        }
    }

    fun isChecked(): Boolean {
        return checked
    }

    fun setEnable(value: Boolean) {
        enable = value
    }
}