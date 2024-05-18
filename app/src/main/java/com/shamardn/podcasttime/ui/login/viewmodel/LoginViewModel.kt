package com.shamardn.podcasttime.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepository
import kotlinx.coroutines.flow.first

class LoginViewModel(
    private val userPrefs: UserPreferenceRepository,
) : ViewModel() {

    suspend fun isUserLoggedIn(): Boolean {
        return userPrefs.isUserLoggedIn().first()
    }
}

class LoginViewModelFactory(
    private val userPreferenceRepository: UserPreferenceRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPreferenceRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}