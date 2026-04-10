package com.khang.badminton.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.khang.badminton.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository {

    //private val db = FirebaseFirestore.getInstance()
    val db = Firebase.firestore

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            db.collection("users")
                .document(user.uid)
                .set(user)
                .await()
            Log.d("Firestore", "SAVE SUCCESS")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("Firestore", "SAVE FAIL: ${e.message}")
            Result.failure(e)
        }
    }
}