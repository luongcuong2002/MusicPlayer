package com.kma.musicplayer.ui.screen.addwidget

import com.kma.musicplayer.R

enum class AddWidgetGuideModel(var img: Int, var title: Int, var content: Int) {
    STEP_1(R.drawable.image_add_widget_guide_1, R.string.step_1, R.string.add_widget_guide_description_1),
    STEP_2(R.drawable.image_add_widget_guide_2, R.string.step_2, R.string.add_widget_guide_description_2),
    STEP_3(R.drawable.image_add_widget_guide_3, R.string.step_3, R.string.add_widget_guide_description_3),
    STEP_4(R.drawable.image_add_widget_guide_4, R.string.step_4, R.string.add_widget_guide_description_4),
}
