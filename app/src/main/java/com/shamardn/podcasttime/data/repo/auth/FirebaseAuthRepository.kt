package com.shamardn.podcasttime.data.repo.auth

import com.shamardn.podcasttime.data.models.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun loginWithEmailAndPassword(
        email: String, password: String
    ): Flow<Resource<String>>
}