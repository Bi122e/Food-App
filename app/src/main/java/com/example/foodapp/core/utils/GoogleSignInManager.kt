package com.example.foodapp.core.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.foodapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleSignInManager @Inject constructor (

    @ApplicationContext private val context: Context
) {

    val googleClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent = googleClient.signInIntent

    fun getAccountFormIntent(data: Intent?): Result<GoogleSignInAccount> {
        return try {
            val account = GoogleSignIn
                .getSignedInAccountFromIntent(data)
                .getResult(ApiException::class.java)
            Result.success(account)
        } catch (e: Exception) {
            Log.e("Error Google Sign In", "${e.message}")
            Result.failure(e)
         }
    }
    fun signOut() {
        googleClient.signOut()
    }
}