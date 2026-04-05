package com.khang.badminton.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch

import com.khang.badminton.data.repository.AuthRepositoryImpl
import com.khang.badminton.utils.AuthState

class AuthViewModel : ViewModel() {

    private val repository = AuthRepositoryImpl()

    private val _loginState = MutableLiveData<AuthState>()
    val loginState: LiveData<AuthState> = _loginState

    fun login(email: String, password: String) {
        _loginState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.login(email, password)

            _loginState.value = result.fold(
                onSuccess = {
                    AuthState.Success("Login success")
                },
                onFailure = {
                    AuthState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun register(email: String, password: String, name: String, phone: String) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.register(email, password)

            _authState.value = result.fold(
                onSuccess = {
                    AuthState.Success("Register success")
                },
                onFailure = {
                    AuthState.Error(it.message ?: "Error")
                }
            )
        }
    }

    fun forgotPassword(email: String) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.forgotPassword(email)

            _authState.value = result.fold(
                onSuccess = {
                    AuthState.Success("Email sent")
                },
                onFailure = {
                    AuthState.Error(it.message ?: "Error")
                }
            )
        }
    }

}