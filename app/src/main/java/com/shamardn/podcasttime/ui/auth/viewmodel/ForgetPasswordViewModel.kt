package com.shamardn.podcasttime.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.shamardn.podcasttime.data.model.Resource
import com.shamardn.podcasttime.domain.repo.auth.FirebaseAuthRepository
import com.shamardn.podcasttime.data.repo.auth.FirebaseAuthRepositoryImpl
import com.shamardn.podcasttime.util.isEmailValid
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ForgetPasswordViewModel(
    val authRepository: FirebaseAuthRepository,
) : ViewModel() {
    private val _forgetPasswordState = MutableSharedFlow<Resource<String>>()
    val forgetPasswordState: SharedFlow<Resource<String>> = _forgetPasswordState.asSharedFlow()


    val email = MutableStateFlow("")

    fun sendUpdatePasswordEmail() = viewModelScope.launch(IO) {
        if (email.value.isEmailValid()) {
            _forgetPasswordState.emit(Resource.Loading)
            authRepository.sendUpdatePasswordEmail(email.value).collect { resource ->
                _forgetPasswordState.emit(resource)
            }
        } else {
            _forgetPasswordState.emit(Resource.Error(Exception("Invalid email")))
        }
    }

}

class ForgetPasswordViewModelFactory(
    private val authRepository : FirebaseAuthRepository = FirebaseAuthRepositoryImpl(),
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForgetPasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ForgetPasswordViewModel(
                authRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}