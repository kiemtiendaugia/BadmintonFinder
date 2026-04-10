package com.khang.badminton.viewmodel

import androidx.lifecycle.*
import com.khang.badminton.data.model.User
import kotlinx.coroutines.launch

import com.khang.badminton.data.repository.AuthRepositoryImpl
import com.khang.badminton.data.repository.UserRepository
import com.khang.badminton.utils.AuthState

class AuthViewModel : ViewModel() {

    private val repository = AuthRepositoryImpl()
    private val userRepository = UserRepository()

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

            result.onSuccess {
                val firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                firebaseUser?.let {
                    val user = User(
                        uid = it.uid,
                        name = name,
                        email = email,
                        phone = phone
                    )

                    userRepository.saveUser(user)
                }

                _authState.value = AuthState.Success("Register success")
            }

            result.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Error")
            }
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

    fun loginWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = repository.firebaseAuthWithGoogle(idToken)

            result.onSuccess {
                val firebaseUser = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser

                firebaseUser?.let {
                    val user = User(
                        uid = it.uid,
                        name = it.displayName ?: "",
                        email = it.email ?: "",
                        avatar = it.photoUrl?.toString() ?: ""
                    )

                    userRepository.saveUser(user)
                }

                _authState.value = AuthState.Success("Login success")
            }

            result.onFailure {
                _authState.value = AuthState.Error(it.message ?: "Error")
            }
        }
    }



}