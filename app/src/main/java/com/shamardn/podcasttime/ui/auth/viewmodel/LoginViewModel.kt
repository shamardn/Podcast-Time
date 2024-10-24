package com.shamardn.podcasttime.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.local.datastore.AppPreferencesDataSource
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.data.model.user.UserDetailsModel
import com.shamardn.podcasttime.domain.repo.auth.FirebaseAuthRepository
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepositoryImpl
import com.shamardn.podcasttime.data.repo.common.AppDataStoreRepositoryImpl
import com.shamardn.podcasttime.domain.repo.common.AppPreferenceRepository
import com.shamardn.podcasttime.domain.repo.user.UserPreferenceRepository
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepositoryImpl
import com.shamardn.podcasttime.domain.mapper.toUserDetailsPreferences
import com.shamardn.podcasttime.util.isEmailValid
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val _loginState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val loginState: SharedFlow<Resource<UserDetailsModel>> = _loginState.asSharedFlow()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val isLoginValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isEmailValid() && password.length >= 6
    }

    fun loginWithEmailAndPassword() = viewModelScope.launch(IO) {
        if (isLoginValid.first()) {
            handleLoginFlow { authRepository.loginWithEmailAndPassword(email.value, password.value) }
        } else {
            _loginState.emit(Resource.Error(Exception("Invalid email or password")))
        }
    }

    fun loginWithGoogle(idToken: String) {
        handleLoginFlow { authRepository.loginWithGoogle(idToken) }
    }

    fun loginWithFacebook(token: String) {
        handleLoginFlow { authRepository.loginWithFacebook(token) }
    }

    private fun handleLoginFlow(loginFlow: suspend () -> Flow<Resource<UserDetailsModel>>) =
        viewModelScope.launch(IO) {
            loginFlow().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        savePreferenceData(resource.data!!)
                        _loginState.emit(Resource.Success(resource.data))
                    }

                    else -> _loginState.emit(resource)
                }
            }
        }

    private suspend fun savePreferenceData(userDetailsModel: UserDetailsModel) {
        appPreferenceRepository.saveLoginState(true)
        userPreferenceRepository.updateUserDetails(userDetailsModel.toUserDetailsPreferences())
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}


class LoginViewModelFactory(
    private val contextValue: Context
) : ViewModelProvider.Factory {

    private val appPreferenceRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(contextValue))
    private val userPreferenceRepository = UserPreferenceRepositoryImpl(contextValue)
    private val authRepository = FirebaseAuthRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return LoginViewModel(
                appPreferenceRepository,
                userPreferenceRepository,
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}