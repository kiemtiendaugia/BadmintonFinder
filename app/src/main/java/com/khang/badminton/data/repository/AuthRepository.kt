package com.khang.badminton.data.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(email: String, password: String): Result<String>
    suspend fun forgotPassword(email: String): Result<String>

    suspend fun firebaseAuthWithGoogle(idToken: String): Result<String>
}