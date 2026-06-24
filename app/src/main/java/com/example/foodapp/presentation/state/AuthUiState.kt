package com.example.foodapp.presentation.state

data class AuthUiState(
    val errorRegister: String? = null,
    val errorLogin: String? = null,

    val isLoadingRegister: Boolean = false,
    val isLoadingLogin: Boolean = false,
)
