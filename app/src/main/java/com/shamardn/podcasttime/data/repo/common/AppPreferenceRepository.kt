package com.shamardn.podcasttime.data.repo.common

import kotlinx.coroutines.flow.Flow

interface AppPreferenceRepository {
    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun isLoggedIn(): Flow<Boolean>
}
