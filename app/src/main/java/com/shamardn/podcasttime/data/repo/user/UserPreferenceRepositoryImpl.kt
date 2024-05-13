package com.shamardn.podcasttime.data.repo.user

import com.shamardn.podcasttime.data.datasource.datastore.UserPreferenceDataSource
import kotlinx.coroutines.flow.Flow

class UserPreferenceRepositoryImpl(
    private val userPreferenceDataSource: UserPreferenceDataSource
) : UserPreferenceRepository {

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        userPreferenceDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun saveUserId(userId: String) {
        userPreferenceDataSource.saveUserId(userId)
    }

    override suspend fun getUserId(): Flow<String?> {
        return userPreferenceDataSource.userId
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> {
        return userPreferenceDataSource.isUserLoggedIn
    }

}