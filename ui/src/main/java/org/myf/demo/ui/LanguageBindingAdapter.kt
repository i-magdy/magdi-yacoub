@file:Suppress("DEPRECATION")

package org.myf.demo.ui

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Build
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton

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


@BindingAdapter("app:checkedLanguageArabic")
fun setSelectedLanguageRadioButtonArabic(
    radio: MaterialRadioButton,
    lang: String
) { radio.isChecked = lang == "ar" }

@BindingAdapter("app:checkedLanguageEnglish")
fun setSelectedLanguageRadioButtonEnglish(
    radio: MaterialRadioButton,
    lang: String
) { radio.isChecked = lang == "en" }

@BindingAdapter("app:changeOutlineColorArabic")
fun setSelectedOutlineColorArabicCard(
    card: MaterialCardView,
    lang: String
) = when(lang == "ar"){
    true -> when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (card.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_dark_secondary)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_light_secondary)

                }
                else -> {}
            }
        } else -> {
            card.strokeColor = card.context.resources.getColor(R.color.md_theme_light_secondary)
        }
    }
    false -> when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (card.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_dark_outlineVariant)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_light_outlineVariant)

                }
                else -> {}
            }
        } else -> {
            card.strokeColor = card.context.resources.getColor(R.color.md_theme_light_outlineVariant)
        }
    }
}

@BindingAdapter("app:changeOutlineColorEnglish")
fun setSelectedOutlineColorEnglishCard(
    card: MaterialCardView,
    lang: String
) = when(lang == "en"){
    true -> when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (card.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_dark_secondary)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_light_secondary)

                }
                else -> {}
            }
        } else -> {
            card.strokeColor = card.context.resources.getColor(R.color.md_theme_light_secondary)
        }
    }
    false -> when{
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            when (card.context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_dark_outlineVariant)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    card.strokeColor = card.context.getColor(R.color.md_theme_light_outlineVariant)

                }
                else -> {}
            }
        } else -> {
            card.strokeColor = card.context.resources.getColor(R.color.md_theme_light_outlineVariant)
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