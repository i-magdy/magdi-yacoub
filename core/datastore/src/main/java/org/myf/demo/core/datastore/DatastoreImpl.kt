package org.myf.demo.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "myf_app_datastore")

class DatastoreImpl private constructor(
    val context: Context
    ){
    companion object {
        private const val APP_SELECTED_LANGUAGE: String = "user_preferred_language"
        private const val APP_STATE: String = "user_registration_state"
        private val APP_STATE_KEY = intPreferencesKey(APP_STATE)
        private val SELECTED_LANGUAGE_KEY = stringPreferencesKey(APP_SELECTED_LANGUAGE)
        fun create(context: Context): DatastoreImpl = DatastoreImpl(context)
    }


    val appLanguage: Flow<String> = context.datastore.data.map { it[SELECTED_LANGUAGE_KEY] ?: "ar" }
    val state: Flow<Int> = context.datastore.data.map { it[APP_STATE_KEY] ?: 0 }
    suspend fun updateState(i: Int) = context.datastore.edit { it[APP_STATE_KEY] = i }
    suspend fun updateAppLanguage(lang: String) = context.datastore.edit { it[SELECTED_LANGUAGE_KEY] = lang }

}