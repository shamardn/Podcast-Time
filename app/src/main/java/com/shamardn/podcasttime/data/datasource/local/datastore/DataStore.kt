package com.shamardn.podcasttime.data.datasource.local.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.shamardn.podcasttime.data.datasource.local.datastore.DataStoreKeys.PODCAST_TIME_PREFERENCES
import com.shamardn.podcasttime.data.datasource.local.datastore.DataStoreKeys.USER_DETAILS_PREFERENCES_PB
import com.shamardn.podcasttime.data.models.user.UserDetailsPreferences
import java.io.InputStream
import java.io.OutputStream

object DataStoreKeys{
    const val PODCAST_TIME_PREFERENCES = "podcast_time_preferences"
    const val USER_DETAILS_PREFERENCES_PB = "user_details.pb"
    val IS_USER_LOGGED_IN = booleanPreferencesKey("is_user_logged_in")
    val LANGUAGE_KEY = stringPreferencesKey("LANGUAGE_KEY")
    val FETCH_TIME_KEY = longPreferencesKey("FETCH_TIME_KEY")
}

val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = PODCAST_TIME_PREFERENCES)


val Context.userDetailsDataStore by dataStore(
    fileName = USER_DETAILS_PREFERENCES_PB, serializer = UserDetailsPreferencesSerializer
)


object UserDetailsPreferencesSerializer : Serializer<UserDetailsPreferences> {

    override val defaultValue: UserDetailsPreferences = UserDetailsPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserDetailsPreferences = try {
        // readFrom is already called on the data store background thread
        UserDetailsPreferences.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: UserDetailsPreferences, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }
}