package com.shamardn.podcasttime.data.datasource.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceDataSource(private val context: Context) {
    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.IS_USER_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.USER_ID] = userId
        }
    }

    val isUserLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DataStoreKeys.IS_USER_LOGGED_IN] ?: false
        }

    val userId: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[DataStoreKeys.USER_ID]
        }

}