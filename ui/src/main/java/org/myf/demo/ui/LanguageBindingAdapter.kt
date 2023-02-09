@file:Suppress("DEPRECATION")

package org.myf.demo.ui

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Build
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

@BindingAdapter("app:selectedLanguageIsArabic")
fun setSelectedLanguageIsArabic(
    button: MaterialButton,
    lang: String
) = when (lang == "ar"){
    true -> when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100,null))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700,null))
            button.strokeWidth = 3
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700))
            button.strokeWidth = 3
        }
    }
    else -> when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            R.color.black_light,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.white,
                            button.context.theme
                        )
                    )
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            android.R.color.white,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.black_light,
                            button.context.theme
                        )
                    )
                }
            }
            button.strokeWidth = 0
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.white))
            button.setTextColor(button.context.resources.getColor(R.color.black_light))
            button.strokeWidth = 0
        }
    }
}


@BindingAdapter("app:selectedLanguageIsEnglish")
fun setSelectedLanguageIsEnglish(
    button: MaterialButton,
    lang: String
) = when (lang == "en"){
    true -> when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100,null))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700,null))
            button.strokeWidth = 3
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.purple_100))
            button.setTextColor(button.context.resources.getColor(R.color.purple_700))
            button.strokeWidth = 3
        }
    }
    else -> when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            R.color.black_light,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.white,
                            button.context.theme
                        )
                    )
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            android.R.color.white,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.black_light,
                            button.context.theme
                        )
                    )
                }
            }
            button.strokeWidth = 0
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.white))
            button.setTextColor(button.context.resources.getColor(R.color.black_light))
            button.strokeWidth = 0
        }
    }
}