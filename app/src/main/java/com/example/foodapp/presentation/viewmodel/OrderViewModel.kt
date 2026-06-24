package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.mapper.toOrder
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.presentation.extentions.toAppNotification
import com.example.foodapp.presentation.state.AppNotificationOrder
import com.example.foodapp.presentation.state.OrderEvent
import com.example.foodapp.presentation.state.OrderUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository,
    private val restaurantRepository: RestaurantRepository,
    private val notificationRepository: NotificationRepository,
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

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val orderUiState = _orderUiState.asStateFlow()

    private val _event = MutableSharedFlow<OrderEvent>()
    val event = _event.asSharedFlow()

    private val handleOrderIds = mutableSetOf<String>()


    init {
        observeOrder()
        observeOrderSuccess()
        Log.d("OrderFlow", "VM HASH = ${hashCode()}")


    }
    private fun observeOrder() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            orderRepository.getOrderByUserId(userId).collectLatest { response ->
                Log.d("OrderVM", "response = $response")
                if (response is ApiResponse.Success) {
                    Log.d("OrderVM", "response = $response")
                    _orderUiState.update { it.copy(order = response.data) }
                    Log.d("OrderVM", "state = ${_orderUiState.value.order}")
                }
            }
        }
    }

    //toi thay the bằng thay vì truyền thẳng, thì khi gọi hàm này tự xử lý để lấy dữ liệu luôn, ui khỏi cần truyền
//    fun placeOrder(cart: Cart, user: User, restaurant: Restaurant, paymentMethod: PaymentMethod) {
    fun placeOrder(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val user = (userRepository.getUserById(userId) as? ApiResponse.Success)?.data ?: return@launch
            val cart = (cartRepository.getCart(userId).first() as? ApiResponse.Success)?.data ?: return@launch
            val restaurant = (restaurantRepository.getRestaurantById(cart.restaurantId) as? ApiResponse.Success)?.data ?: return@launch

            val foods = cart.cartItems.mapNotNull {

                (foodRepository.getFoodById(it.foodId) as? ApiResponse.Success)?.data
            }

            val foods1 = coroutineScope {
                cart.cartItems.map { item ->
                    async {
                        (foodRepository.getFoodById(item.foodId) as? ApiResponse.Success)?.data
                    }
                }.awaitAll().filterNotNull()
            }
            val order = toOrder(
                cart = cart,
                user = user,
                restaurant = restaurant,
                foods = foods1,
            )
            val result = orderRepository.createOrder(order, paymentMethod)
            if (result is ApiResponse.Success) {
                _event.emit(OrderEvent.NavigationToDetail(result.data))
                Log.d("place order", "success")
            } else if (result is ApiResponse.Error) {
                Log.d("place order", "failed ${result.message}")
            }
        }
    }

    private fun loadFood(foodId: String) {
        viewModelScope.launch {
            val result = foodRepository.getFoodById(foodId)
            if (result is ApiResponse.Success) {
                _orderUiState.update {
                    it.copy(foods = it.foods + result.data)
                }
            }
        }
    }


    private fun observeOrderSuccess() {
        Log.d("checkVM_observeOrderSuccess", " CALLED")

        viewModelScope.launch {
            Log.d("checkVM_observeOrderSuccess", "VM: observeOrderSuccess - run")

            val userId = authRepository.currentUserId() ?: return@launch

            orderRepository.getOrderNeedRating(userId).collectLatest { response ->
                Log.d("checkVM_observeOrderSuccess", "VM: userId = $userId")
                when (response) {
                    is ApiResponse.Success -> {
                        Log.d(
                            "checkVM_observeOrderSuccess",
                            "BEFORE FILTER handledIds = $handleOrderIds"
                        )
                       val handleOrders = response.data.filter {
                           it.orderId !in handleOrderIds
                        }

                        Log.d(
                            "checkVM_observeOrderSuccess",
                            "AFTER FILTER handleOrders = ${handleOrders.map { it.orderId }}"
                        )

                        Log.d("checkVM_observeOrderSuccess", "VM: check is Empty ${response.data} == ${response.data.isNotEmpty()}")
                        if (handleOrders.isNotEmpty()) {
                            Log.d("checkVM_observeOrderSuccess", " uccess: ${response.data}")
                            _orderUiState.update {
                                it.copy(
                                    appNotificationOrder = AppNotificationOrder(
                                        orders = handleOrders,
//                                    isRead = false,
                                        ratingNotificationSent = true
                                    )
                                )
                            }
                            handleOrders.forEach { order ->
                                val response = notificationRepository.createNotification(notification = order.toAppNotification())

                                when (response) {
                                    is ApiResponse.Success -> {
                                        Log.d("check_VM_createNotification", "success")
                                    }
                                    is ApiResponse.Error -> {
                                        Log.d("check_VM_createNotification", "error ${response.message}")
                                    }
                                    else -> {
                                        Log.d("check_VM_createNotification", "else")
                                    }
                                }
                            }

                            handleOrders.forEach {
                                handleOrderIds.add(it.orderId)
                            }
                        }
                    }
                    is ApiResponse.Error -> {
                        Log.d("checkVM_observeOrderSuccess", " error: ${response.message}")
                    }
                    else -> {
                        Log.d("checkVM_observeOrderSuccess", " else")
                    }
                }
                Log.d("checkVM_observeOrderSuccess", " state: ${orderUiState.value.appNotificationOrder}")
            }
        }
    }

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
        } ?: originalOrders

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
            val result = response.toUiState()
            _cancelOrderEvent.emit(result)

            if (result is UiState.Success) {
                loadOrders(userId = "")
            }
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


    fun resetNotification() {
        viewModelScope.launch {
            _orderUiState.value.appNotificationOrder?.orders?.forEach {
                orderRepository.updateOrder(
                    order = it.copy(
                        ratingNotificationSent = true
                    )
                )
            }

            _orderUiState.update {
                it.copy(
                    appNotificationOrder = it.appNotificationOrder?.copy(ratingNotificationSent = false)
                )
            }
        }
    }
}


data class OrderStatistics(
    val totalOrders: Int,
    val totalSpent: Long,
)

