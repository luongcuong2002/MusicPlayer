package com.kma.musicplayer.ui.screen.addwidget

import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.children
import androidx.viewpager2.widget.ViewPager2
import com.kma.musicplayer.databinding.ActivityAddWidgetBinding
import com.kma.musicplayer.ui.screen.core.BaseActivity
import com.kma.musicplayer.R
import com.kma.musicplayer.model.Theme

class AddWidgetActivity : BaseActivity<ActivityAddWidgetBinding>() {

    private var addWidgetAdapter: AddWidgetGuideAdapter? = null

    override fun getContentView(): Int = R.layout.activity_add_widget

    override fun onThemeChanged(theme: Theme) {
        binding.root.setBackgroundColor(getColor(theme.backgroundColorRes))
        binding.tvTitle.setTextColor(getColor(theme.titleTextColorRes))
        binding.backButton.setImageResource(theme.imageBackButtonRes)
        addWidgetAdapter?.setTheme(theme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewPager()
        setupListeners()
    }

    private fun setupViewPager() {
        addWidgetAdapter = AddWidgetGuideAdapter()
        binding.viewPager.adapter = addWidgetAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicator(position)
                if (position == addWidgetAdapter!!.itemCount - 1) {
                    binding.tvNext.text = getString(R.string.got_it)
                } else {
                    binding.tvNext.text = getString(R.string.next)
                }
            }
        })
    }

    private fun setupListeners() {
        binding.llNext.setOnClickListener {
            if (binding.viewPager.currentItem < addWidgetAdapter!!.itemCount - 1) {
                binding.viewPager.currentItem += 1
            } else {
                finish()
            }
        }
    }

    private fun updateIndicator(position: Int) {
        for (i in 0 until binding.llIndicator.childCount) {
            val imageView = binding.llIndicator.children.elementAt(i)
            if (i == position) {
                imageView.setBackgroundResource(R.drawable.bg_pager_indicator_active)
            } else {
                imageView.setBackgroundResource(R.drawable.bg_pager_indicator_inactive)
            }
        }
    }
}