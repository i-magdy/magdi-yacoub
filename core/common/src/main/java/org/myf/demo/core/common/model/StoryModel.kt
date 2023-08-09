package org.myf.demo.core.common.model

import androidx.annotation.Keep

@Keep class StoryModel(){
    var title: String = ""
    var supportText: String = ""
    var img: String = ""
    var url: String = ""

    constructor(
        title: String,
        img: String,
        supportText: String,
        url: String
    ): this(){
        this.title = title
        this.img = img
        this.supportText = supportText
        this.url = url
    }

}