package com.shamardn.podcasttime.data.repo.user

import com.shamardn.podcasttime.data.models.Resource
import com.shamardn.podcasttime.data.models.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFireStoreRepository {
      suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>
}