package com.example.foodapp.presentation.state

sealed class SuccessSharedState {

    data object DELIVERED: SuccessSharedState()
}