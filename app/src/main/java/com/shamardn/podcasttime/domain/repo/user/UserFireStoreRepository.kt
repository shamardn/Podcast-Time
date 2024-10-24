package com.shamardn.podcasttime.domain.repo.user

import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.data.model.user.UserDetailsModel
import kotlinx.coroutines.flow.Flow

interface UserFireStoreRepository {
      suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>>
}