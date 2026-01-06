package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date

class UsersRepositoryImpl: UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val userRef = firestore.collection(Constance.COLLECTION_USER)

    override fun getCurrentUser(userId: String): Flow<ApiResponse<User>> = callbackFlow {
        val listener = userRef
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ApiResponse.Error(error.message ?: "Unknow"))
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject(User::class.java)
                if (user != null && user.isValid()) {
                    trySend(ApiResponse.Success(user))
                } else {
                    trySend(ApiResponse.Empty)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun createUser(user: User): ApiResponse<Unit> {
        return  try {
            userRef.document(user.uid).set(user).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "failed to create user")
        }
    }

    override suspend fun updateUser(user: User): ApiResponse<Unit> {
        return try {
            userRef.document(user.uid).set(user).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update user")
        }
    }

    override suspend fun updateProfileImage(userId: String, imageUrl: String): ApiResponse<Unit> {
        return try {
            userRef.document(userId).update("profileUrl", imageUrl).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update profile image")
        }
    }

    override suspend fun checkEmailExist(email: String): ApiResponse<Boolean> {
        return try {
            val userDoc = userRef
                .whereEqualTo("email", email)
                .orderBy("email")
                .limit(1)
                .get()
                .await()
//            if (userDoc != null && !userDoc.isEmpty) {
//                ApiResponse.Success(true)
//            } else {
//                ApiResponse.Success(false)
//            }
            ApiResponse.Success(!userDoc.isEmpty)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to check email")
        }
    }

    override suspend fun updateFcmToken(userId: String, token: String): ApiResponse<Unit> {
        return try {
            userRef.document(userId)
                .update("fcmToken",token)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to update fcm token")
        }
    }

    override suspend fun deactivateAccount(userId: String): ApiResponse<Unit> {
        return try {
            userRef.document(userId)
                .update("isActive", false)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to delete account")
        }
    }
}