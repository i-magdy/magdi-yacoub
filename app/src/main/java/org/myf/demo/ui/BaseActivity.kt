package org.myf.demo.ui

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.myf.demo.core.datastore.DatastoreImpl
import java.util.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity {

    constructor(): super()
    constructor(contentLayoutId: Int): super(contentLayoutId)

    @Inject
    lateinit var datastoreImpl: DatastoreImpl

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this::datastoreImpl.isInitialized) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    datastoreImpl.appLanguage.collect {
                        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            resources.configuration.locales[0].language
                        } else {
                            resources.configuration.locale.language
                        }
                        if (lang != it) {
                            changeAppLanguage(it)
                        }
                    }
                }
            }
        }
    }

    private fun changeAppLanguage(lang: String){
        val appLocale = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(appLocale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSystemService(LocaleManager::class.java).applicationLocales = LocaleList(Locale.forLanguageTag(lang))
        }
        recreate()
    }
}