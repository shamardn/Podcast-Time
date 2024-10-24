package com.shamardn.podcasttime.ui.common.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.shamardn.podcasttime.data.datasource.local.datastore.AppPreferencesDataSource
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.repo.auth.FirebaseAuthRepository
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepositoryImpl
import com.shamardn.podcasttime.data.repo.common.AppDataStoreRepositoryImpl
import com.shamardn.podcasttime.domain.repo.common.AppPreferenceRepository
import com.shamardn.podcasttime.domain.repo.user.UserFireStoreRepository
import com.shamardn.podcasttime.domain.repo.user.UserPreferenceRepository
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepositoryImpl
import com.shamardn.podcasttime.domain.mapper.toUserDetailsModel
import com.shamardn.podcasttime.domain.mapper.toUserDetailsPreferences
import com.shamardn.podcasttime.util.CrashlyticsUtils
import com.shamardn.podcasttime.util.CrashlyticsUtils.LISTEN_TO_USER_DETAILS
import com.shamardn.podcasttime.util.UserDetailsException
import com.training.ecommerce.data.repository.user.UserFireStoreRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(
    private val appPreferencesRepository: AppPreferenceRepository,
    private val userPreferencesRepository: UserPreferenceRepository,
    private val userFireStoreRepository: UserFireStoreRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val logoutState = MutableSharedFlow<Resource<Unit>>()

    // load user data in state flow inside view model  scope
    val userDetailsState = getUserDetails().stateIn(
        viewModelScope, started = SharingStarted.Eagerly, initialValue = null
    )

    init {
        listenToUserDetails()
    }

    // load user data flow
    // we can use this to get user data in the view in main thread so we do not want to wait the data from state
    // note that this flow block the main thread while you get the data every time you call it
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserDetails() =
        userPreferencesRepository.getUserDetails().mapLatest { it.toUserDetailsModel() }

    private fun listenToUserDetails() = viewModelScope.launch {
        val userId = userPreferencesRepository.getUserId().first()
        if (userId.isEmpty()) return@launch
        userFireStoreRepository.getUserDetails(userId).catch { e ->
            val msg = e.message ?: "Error listening to user details"
            CrashlyticsUtils.sendCustomLogToCrashlytics<UserDetailsException>(
                msg, LISTEN_TO_USER_DETAILS to msg
            )
            if (e is UserDetailsException) logOut()
        }.collectLatest { resource ->
            Log.d(TAG, "listenToUserDetails: ${resource.data}")
            when (resource) {
                is Resource.Success -> {

                    resource.data?.let {
                        userPreferencesRepository.updateUserDetails(it.toUserDetailsPreferences())
                    }
                }

                else -> {
                    // Do nothing
                    Log.d(TAG, "Error listen to user details: ${resource.exception?.message}")
                }
            }
        }
    }

    suspend fun isUserLoggedIn() = appPreferencesRepository.isLoggedIn()
    suspend fun logOut() = viewModelScope.launch {
        logoutState.emit(Resource.Loading)
        firebaseAuthRepository.logout()
        userPreferencesRepository.clearUserPreferences()
        appPreferencesRepository.saveLoginState(false)
        logoutState.emit(Resource.Success(Unit))
    }

    companion object {
        private const val TAG = "UserViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: UserViewModel cleared")
    }
}

class UserViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    private val appPreferencesRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(context))
    private val userPreferencesRepository = UserPreferenceRepositoryImpl(context)
    private val userFireStoreRepository = UserFireStoreRepositoryImpl()
    private val firebaseAuthRepository = FirebaseAuthRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(
                appPreferencesRepository,
                userPreferencesRepository,
                userFireStoreRepository,
                firebaseAuthRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}