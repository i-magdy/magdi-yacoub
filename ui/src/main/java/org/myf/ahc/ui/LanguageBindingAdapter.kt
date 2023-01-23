package org.myf.ahc.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Build
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

@BindingAdapter("app:selectedLanguageIsArabic")
fun setSelectedLanguageIsArabic(
    button: MaterialButton,
    lang: String
){
    if (lang == "ar") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100,null))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700,null))
        }else{
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700))
        }
        button.strokeWidth = 3
    }else{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when(button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.black_light,button.context.theme))
                    button.setTextColor(button.context.resources.getColor(R.color.white,button.context.theme))
                }
                else -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(android.R.color.white,button.context.theme))
                    button.setTextColor(button.context.resources.getColor(R.color.black_light,button.context.theme))
                }
            }
        }else{
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700))
        }
        button.strokeWidth = 0
    }
}

@SuppressLint("ResourceType")
@BindingAdapter("app:selectedLanguageIsEnglish")
fun setSelectedLanguageIsEnglish(
    button: MaterialButton,
    lang: String
){
    if (lang == "en") {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100,null))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700,null))
        }else{
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700))
        }
        button.strokeWidth = 3
    }else{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when(button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.black_light,button.context.theme))
                    button.setTextColor(button.context.resources.getColor(R.color.white,button.context.theme))
                }
                else -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(android.R.color.white,button.context.theme))
                    button.setTextColor(button.context.resources.getColor(R.color.black_light,button.context.theme))
                }
            }
        }else{
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.white))
            button.setTextColor(button.context.resources.getColor(R.color.white))
        }
        button.strokeWidth = 0
    }
}