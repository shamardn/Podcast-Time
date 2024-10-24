package com.training.ecommerce.data.repository.user

import com.google.firebase.firestore.FirebaseFirestore
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.data.model.user.UserDetailsModel
import com.shamardn.podcasttime.domain.repo.user.UserFireStoreRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserFireStoreRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserFireStoreRepository {

    override suspend fun getUserDetails(userId: String): Flow<Resource<UserDetailsModel>> =
        callbackFlow {
            send(Resource.Loading)
            val documentPath = "users/$userId"
            val document = firestore.document(documentPath)
            val listener = document.addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                value?.toObject(UserDetailsModel::class.java)?.let {
                    if(it.disabled == true) {
                        close(AccountDisabledException("Account Disabled"))
                        return@addSnapshotListener
                    }
                    trySend(Resource.Success(it))
                } ?: run {
                    close(UserNotFoundException("User not found"))
                }
            }
            awaitClose {
                listener.remove()
            }
        }
}

class UserNotFoundException(message: String) : Exception(message)
class AccountDisabledException(message: String) : Exception(message)