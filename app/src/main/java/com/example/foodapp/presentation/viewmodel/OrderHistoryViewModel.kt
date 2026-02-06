package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cart: CartRepository,
    private val user: UserRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<UiState<List<Order>>>(UiState.Loading)
    val orders = _orders.asStateFlow()

    private val _selectOrder = MutableStateFlow<UiState<Order>>(UiState.Idle)
    val selectOrder = _selectOrder.asStateFlow()

    //  private val _cancelOrderEvent = MutableSharedFlow<UiState<Unit>>()
    //  tránh mất event khi UI chưa collect kịp.
    private val _cancelOrderEvent =
        MutableSharedFlow<UiState<Unit>>(replay = 0, extraBufferCapacity = 1)
    val cancelOrderEvent = _cancelOrderEvent.asSharedFlow()

    private val _statistics = MutableStateFlow<UiState<OrderStatistics>>(UiState.Loading)
    val statistics = _statistics.asStateFlow()
    private var originalOrders: List<Order> = emptyList()
    private var loadOrdersJob: Job? = null

    fun loadOrders(userId: String) {
        loadOrdersJob?.cancel()

        loadOrdersJob = viewModelScope.launch {
            _orders.value = UiState.Loading

            orderRepository.getOrderByUserId(userId)
                .collectLatest { response ->
                    val uiState = response.toUiState()
                    _orders.value = uiState
                    if (response is ApiResponse.Success) {
                        originalOrders = response.data
                    }
                }
        }
    }

    fun loadOrdersByStatus(userId: String, status: OrderStatus) {
        viewModelScope.launch {
            _orders.value = UiState.Loading
            _orders.value = orderRepository.getOrderByStatus(userId, status).toUiState()
        }
    }

//        fun filterOrders(status: OrderStatus?) {
//        if (originalOrders.isEmpty()) return
//
//        val filtered = status?.let {
//            originalOrders.filter { order ->
//                order.status == it
//            } ?: originalOrders
//        }
//
//        _orders.value = UiState.Success(filtered)
//    }
fun filterOrders(status: OrderStatus?) {
    if (originalOrders.isEmpty()) return
    val filtered = status?.let {
        originalOrders.filter { order ->
            order.status == it
        }
    }?: originalOrders

    _orders.value = UiState.Success(filtered)
}



    fun loadOrderDetail(orderId: String) {
        viewModelScope.launch {
            _selectOrder.value = UiState.Loading
            _selectOrder.value = orderRepository.getOrderById(orderId).toUiState()
        }
    }

    fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            _cancelOrderEvent.emit(UiState.Loading)
            val response = orderRepository.cancelOrder(orderId)
            val uiState = response.toUiState()
            _cancelOrderEvent.emit(uiState)
        }
    }

    fun loadStatistics(userId: String) {
        viewModelScope.launch {
            _statistics.value = UiState.Loading

            val orderState = orderRepository.getTotalOrdersCount(userId).toUiState()
            val spendState = orderRepository.getTotalSpent(userId).toUiState()

            when {
                orderState is UiState.Error ->
                    _statistics.value = orderState

                spendState is UiState.Error ->
                    _statistics.value = spendState

                orderState is UiState.Success && spendState is UiState.Success -> {
                    _statistics.value = UiState.Success(
                        OrderStatistics(
                            totalOrders = orderState.data,
                            totalSpent = spendState.data
                        )
                    )
                }
            }
        }
    }

    fun resetCancelState() {
        viewModelScope.launch {
            _cancelOrderEvent.emit(UiState.Idle)
        }
    }
}

data class OrderStatistics(
    val totalOrders: Int,
    val totalSpent: Int,
)

