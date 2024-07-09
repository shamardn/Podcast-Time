package com.shamardn.podcasttime.ui.auth.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.shamardn.podcasttime.data.models.Resource
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepository
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepository
import com.shamardn.podcasttime.util.isEmailValid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    val loginState = MutableSharedFlow<Resource<String>>()

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    val isGoogleBtnClicked = MutableLiveData(false)

    private val isLoginValid: Flow<Boolean> = combine(email, password) { email, password ->
        email.isEmailValid() && password.length >= 6
    }

    fun onClickGoogleBtn() {
        isGoogleBtnClicked.postValue(true)
    }

    fun login() {
        viewModelScope.launch {
            if (isLoginValid.first()) {

                authRepository.loginWithEmailAndPassword(email.value, password.value)
                    .onEach { resource ->

                        when (resource) {
                            is Resource.Loading -> {
                                loginState.emit(Resource.Loading)
                            }

                            is Resource.Success -> {
                                userPrefs.saveLoginState(true)
                                userPrefs.saveUserId(resource.data ?: "Empty User Id")
                                loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                            }

                            is Resource.Error -> {
                                loginState.emit(
                                    Resource.Error(
                                        resource.exception ?: Exception("Unknown Exception")
                                    )
                                )
                            }
                        }
                    }.launchIn(this)
            } else {
                loginState.emit(Resource.Error(Exception("Invalid Email or Password")))
            }
        }
    }

    fun loginWithGoogle(tokenId: String) = viewModelScope.launch {
        isGoogleBtnClicked.postValue(false)

        authRepository.loginWithGoogle(tokenId).onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    loginState.emit(Resource.Loading)
                }

                is Resource.Success -> {
                    userPrefs.saveLoginState(true)
                    userPrefs.saveUserId(resource.data ?: "Empty User Id")
                    loginState.emit(Resource.Success(resource.data ?: "Empty User Id"))
                }

                is Resource.Error -> {
                    loginState.emit(
                        Resource.Error(
                            resource.exception ?: Exception("Unknown Exception")
                        )
                    )
                }
            }
        }.launchIn(this)
    }

}

class LoginViewModelFactory(
    private val userPrefs: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPrefs, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}