package com.shamardn.podcasttime.data.datasource.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shamardn.podcasttime.data.datasource.datastore.DataStoreKeys.PODCAST_TIME_PREFERENCES

object DataStoreKeys{
    const val PODCAST_TIME_PREFERENCES = "podcast_time_preferences"
    val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
    val USER_ID = stringPreferencesKey("user_id")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PODCAST_TIME_PREFERENCES)