package com.shamardn.podcasttime.ui.auth.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.datasource.datastore.AppPreferencesDataSource
import com.shamardn.podcasttime.data.models.Resource
import com.shamardn.podcasttime.data.models.user.UserDetailsModel
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepository
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepositoryImpl
import com.shamardn.podcasttime.data.repo.common.AppDataStoreRepositoryImpl
import com.shamardn.podcasttime.data.repo.common.AppPreferenceRepository
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepository
import com.shamardn.podcasttime.data.repo.user.UserPreferenceRepositoryImpl
import com.shamardn.podcasttime.util.isEmailValid
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val appPreferenceRepository: AppPreferenceRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val authRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val _registerState = MutableSharedFlow<Resource<UserDetailsModel>>()
    val registerState: SharedFlow<Resource<UserDetailsModel>> = _registerState.asSharedFlow()

    val name = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val isRegisterValid = combine(
        name, email, password, confirmPassword
    ) { name, email, password, confirmPassword ->
        name.isNotEmpty()
                && email.isEmailValid()
                && password.length >= 6
                && confirmPassword.isNotEmpty()
                && confirmPassword == password
    }

    fun registerWithEmailAndPassword() = viewModelScope.launch(IO){
        if (isRegisterValid.first()){
            authRepository.registerWithEmailAndPassword(name.value, email.value, password.value).collect {
                _registerState.emit(it)
            }
        }else {

        }
    }

    fun registerWithGoogle(idToken: String) = viewModelScope.launch {
            authRepository.registerWithGoogle(idToken).collect {
                _registerState.emit(it)
            }
        }
}

class RegisterViewModelFactory(
    private val contextValue: Context
) : ViewModelProvider.Factory {

    private val appPreferenceRepository =
        AppDataStoreRepositoryImpl(AppPreferencesDataSource(contextValue))
    private val userPreferenceRepository = UserPreferenceRepositoryImpl(contextValue)
    private val authRepository = FirebaseAuthRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return RegisterViewModel(
                appPreferenceRepository,
                userPreferenceRepository,
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}