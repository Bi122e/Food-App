package com.example.foodapp.presentation.state

sealed interface CompleteEventState {

    data object Success : CompleteEventState
    data class Error(val message: String) : CompleteEventState

}