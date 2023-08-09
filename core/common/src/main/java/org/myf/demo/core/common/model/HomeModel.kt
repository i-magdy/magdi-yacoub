package org.myf.demo.core.common.model

import androidx.annotation.Keep

@Keep class HomeModel() {
    val coverUrl1: String = ""
    val coverUrl2: String = ""
    val coverUrl3: String = ""
    val knowMoreUrl: String = ""
    val stories: List<StoryModel> = emptyList()
}