package com.shamardn.podcasttime.data.repo.common

import com.shamardn.podcasttime.data.datasource.local.datastore.AppPreferencesDataSource
import com.shamardn.podcasttime.domain.repo.common.AppPreferenceRepository
import kotlinx.coroutines.flow.Flow

class AppDataStoreRepositoryImpl(
    private val appPreferencesDataSource: AppPreferencesDataSource
) : AppPreferenceRepository {

    override suspend fun saveLoginState(isLoggedIn: Boolean) {
        appPreferencesDataSource.saveLoginState(isLoggedIn)
    }

    override suspend fun isLoggedIn(): Flow<Boolean> {
        return appPreferencesDataSource.isUserLoggedIn
    }
}
