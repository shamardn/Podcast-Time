package com.shamardn.podcasttime.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepository

class UserViewModel(
    private val userPreferenceRepository: UserPreferenceRepository
): ViewModel() {
    suspend fun isUserLoggedIn() = userPreferenceRepository.isUserLoggedIn()
}

class UserViewModelFactory(
    private val userPreferenceRepository: UserPreferenceRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(UserViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userPreferenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}