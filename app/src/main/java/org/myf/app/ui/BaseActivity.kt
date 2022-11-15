package org.myf.app.ui

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.*

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appLocale = LocaleListCompat.forLanguageTags("ar")
        AppCompatDelegate.setApplicationLocales(appLocale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSystemService(LocaleManager::class.java).applicationLocales = LocaleList(Locale.forLanguageTag("ar"))
        }
    }
}