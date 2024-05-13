package com.shamardn.podcasttime.ui.login.viewmodel

import androidx.lifecycle.ViewModel
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepository
import kotlinx.coroutines.flow.first

class LoginViewModel(
    private val userPrefs: UserPreferenceRepository,
) : ViewModel() {

    suspend fun isUserLoggedIn(): Boolean {
        return userPrefs.isUserLoggedIn().first()
    }
}