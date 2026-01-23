package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.Constance
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl: UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userRef = firestore.collection(Constance.COLLECTION_USER)

    override suspend fun getUserById(userId: String): ApiResponse<User> {
        return try {
            val userDoc = userRef.document(userId).get().await()
            val user = userDoc.toObject(User::class.java)
                ?: return ApiResponse.Error("User not found")
            ApiResponse.Success(user)
//            val user = userDoc.toObject(User::class.java)
//                ?: return ApiResponse.Error("User not found")
//                ApiResponse.Success(user)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Failed to get user by id")
        }
    }

    override suspend fun getCurrentUser(userId: String): Flow<ApiResponse<User>> = callbackFlow {
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

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): ApiResponse<Unit> = suspendCoroutine{ continuation ->

        val user = firebaseAuth.currentUser
            ?: return@suspendCoroutine continuation.resume(
                ApiResponse.Error("User chua dang nhap")
            )

        val email = user.email
            ?: return@suspendCoroutine continuation.resume(
                ApiResponse.Error("Khong tim thay email")
            )
        val credential = EmailAuthProvider
            .getCredential(email, oldPassword)

        user.reauthenticate(credential)
            .addOnSuccessListener {
                continuation.resume(ApiResponse.Success(Unit))
            }
            .addOnFailureListener {
                continuation.resume(ApiResponse.Error(it.message ?: "Doi mat khau that bai"))
            }
    }


    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}