package com.example.foodapp.core

import com.example.foodapp.domain.model.User

sealed class AuthResult {
    data class LoggedIn(val user: User): AuthResult()
    object LoggedOut: AuthResult()
    object PasswordChange: AuthResult()
    object Idle: AuthResult()
    object NotLoggedIn: AuthResult()
}