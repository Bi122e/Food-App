package com.example.foodapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    //luu item dang thao tac, để khi quay lại giữ id
    //luu null de dễ biểu diễn trong xử lý hơn
    var pendingItem: String? by mutableStateOf(null)

    // Luu route dang cho xu ly sau khi complete profile
    var pendingRoute: String? by mutableStateOf(null)

    fun savePendingRoute(route: String) {
        pendingRoute = route
    }

    fun savePendingItem(item: String) {
        pendingItem = item
    }
    fun consumePendingRoute(): String? {
        val route = pendingRoute
        pendingRoute = null
        return route
    }
}