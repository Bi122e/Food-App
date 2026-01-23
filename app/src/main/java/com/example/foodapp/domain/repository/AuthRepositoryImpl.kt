package com.example.foodapp.domain.repository

import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String): ApiResponse<Unit> {
       return try {
           auth.signInWithEmailAndPassword(email, password).await()
           ApiResponse.Success(Unit)
       } catch (e: Exception) {
           ApiResponse.Error(e.message ?: "Login Failed")
       }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): ApiResponse<Unit> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return ApiResponse.Error("User null")

            val userData = User(
                uid = user.uid,
                name = name,
                email = email
            )
            firestore.collection("users")
                .document()
                .set(userData)
                .await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Register Failed")
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): ApiResponse<Unit> {
        return try {
            val user = auth.currentUser ?: return ApiResponse.Error("Not Logged in")
            user.updatePassword(newPassword).await()
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Change password failed")
        }
    }
    override suspend fun logOut(){
        auth.signOut()
    }


    override suspend fun loginWithGoogle(idToken: String): ApiResponse<Unit> {
        return try {
            //login firebase bang google
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()

            val firebaseUser = authResult.user
                ?: return ApiResponse.Error("User null")

            //check user ton tai trong fb hay chua
            val userRef = firestore.collection("users").document(firebaseUser.uid)
            val snapshot = userRef.get().await()

            //neu chua ton tai thi tao moi (register)
            if (!snapshot.exists()) {
                val userData = com.example.foodapp.domain.model.User(
                    uid = firebaseUser.uid,
                    name = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                )
                userRef.set(userData).await()
            }
            ApiResponse.Success(Unit)
        } catch (e: Exception) {
            ApiResponse.Error(e.message ?: "Logging with Google Failed")
        }
    }

}