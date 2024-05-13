package com.shamardn.podcasttime.data.repo.user

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    suspend fun isUserLoggedIn(): Flow<Boolean>
    suspend fun saveLoginState(isLoggedIn: Boolean)
    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): Flow<String?>
}