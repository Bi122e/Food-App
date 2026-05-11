package com.example.foodapp.presentation.state

sealed class OrderEvent {
    data class NavigationToDetail(val orderId: String): OrderEvent()
}
