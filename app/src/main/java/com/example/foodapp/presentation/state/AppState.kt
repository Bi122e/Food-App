package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.User

sealed class AppState {
    object Loading : AppState()
    object Guest : AppState()
    data class  NeedCompleteProfile(val user: User): AppState()
    data class LoggedIn(val user: User) : AppState()
    data class Error(val message: String): AppState()

}
