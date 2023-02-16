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
            when (button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_dark_tertiaryContainer,null))
                    button.setTextColor(button.context.resources.getColor(R.color.md_theme_dark_onTertiaryContainer,null))
                    button.strokeWidth = 2
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_light_tertiaryContainer,null))
                    button.setTextColor(button.context.resources.getColor(R.color.md_theme_light_onTertiaryContainer,null))
                    button.strokeWidth = 2
                }
                else -> {}
            }
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_light_tertiary))
            button.setTextColor(button.context.resources.getColor(R.color.md_theme_light_onTertiary))
            button.strokeWidth = 2
        }
    }
    else -> when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            R.color.md_theme_dark_surface,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.md_theme_dark_onSurface,
                            button.context.theme
                        )
                    )
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            R.color.md_theme_light_surface,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.md_theme_light_onSurface,
                            button.context.theme
                        )
                    )
                }
            }
            button.strokeWidth = 0
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_light_surface))
            button.setTextColor(button.context.resources.getColor(R.color.md_theme_light_onSurface))
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
            when (button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_dark_tertiaryContainer,null))
                    button.setTextColor(button.context.resources.getColor(R.color.md_theme_dark_onTertiaryContainer,null))
                    button.strokeWidth = 2
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_light_tertiaryContainer,null))
                    button.setTextColor(button.context.resources.getColor(R.color.md_theme_light_onTertiaryContainer,null))
                    button.strokeWidth = 2
                }
                else -> {}
            }
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_light_tertiary))
            button.setTextColor(button.context.resources.getColor(R.color.md_theme_light_onTertiary))
            button.strokeWidth = 2
        }
    }
    else -> when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (button.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            R.color.md_theme_dark_surface,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.md_theme_dark_onSurface,
                            button.context.theme
                        )
                    )
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    button.backgroundTintList = ColorStateList.valueOf(
                        button.context.resources.getColor(
                            R.color.md_theme_light_surface,
                            button.context.theme
                        )
                    )
                    button.setTextColor(
                        button.context.resources.getColor(
                            R.color.md_theme_light_onSurface,
                            button.context.theme
                        )
                    )
                }
            }
            button.strokeWidth = 0
        }
        else -> {
            button.backgroundTintList = ColorStateList.valueOf(button.context.resources.getColor(R.color.md_theme_light_surface))
            button.setTextColor(button.context.resources.getColor(R.color.md_theme_light_onSurface))
            button.strokeWidth = 0
        }
    }
}