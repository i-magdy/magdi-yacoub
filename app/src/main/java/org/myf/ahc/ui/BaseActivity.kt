package org.myf.ahc.ui

import android.app.LocaleManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import org.myf.ahc.core.datastore.DatastoreImpl
import java.util.*
import javax.inject.Inject

open class BaseActivity : AppCompatActivity {

    constructor(): super()
    constructor(contentLayoutId: Int): super(contentLayoutId)

   @Inject
    lateinit var datastoreImpl: DatastoreImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this::datastoreImpl.isInitialized) {
            lifecycleScope.launchWhenCreated {
                datastoreImpl.appLanguage.collect {
                    val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        resources.configuration.locales[0].language
                    }else{
                        resources.configuration.locale.language
                    }
                    if (lang != it){
                        changeAppLanguage(it)
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