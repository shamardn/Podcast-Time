package com.shamardn.podcasttime.data.datasource.datastore


import javax.inject.Inject

interface AppConfiguration {
    suspend fun getCurrentLanguage(): String?
    suspend fun saveCurrentLanguage(value: String)

}

class AppConfigurator @Inject constructor(private val dataStorePreferences: DataStorePreferences) :
    AppConfiguration {

    override suspend fun getCurrentLanguage(): String? {
        return dataStorePreferences.readString(LANGUAGE_KEY)
    }

    override suspend fun saveCurrentLanguage(value: String) {
        dataStorePreferences.writeString(LANGUAGE_KEY, value)
    }

    companion object DataStorePreferencesKeys {
        const val LANGUAGE_KEY = "LANGUAGE_KEY"
    }
}