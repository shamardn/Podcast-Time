package com.shamardn.podcasttime.data.datasource.local.datastore


import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface AppConfiguration {
    suspend fun getCurrentLanguage(): String
    suspend fun saveCurrentLanguage(language: String)

}

class AppConfigurator @Inject constructor(private val context: Context) :
    AppConfiguration {
        private val currentLanguage: Flow<String> = context.appDataStore.data.map { preferences ->
            preferences[DataStoreKeys.LANGUAGE_KEY] ?: "en"
        }

    override suspend fun getCurrentLanguage(): String {
       return currentLanguage.first()
    }

    override suspend fun saveCurrentLanguage(language: String) {
        context.appDataStore.edit { preferences ->
            preferences[DataStoreKeys.LANGUAGE_KEY] = language
        }
    }
}