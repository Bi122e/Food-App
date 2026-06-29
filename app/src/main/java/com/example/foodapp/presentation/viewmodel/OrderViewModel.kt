package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.ChatRepository
import com.example.foodapp.data.repository.FoodRepository
import com.example.foodapp.data.repository.NotificationRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.mapper.toOrder
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.presentation.extentions.toAppNotification
import com.example.foodapp.presentation.state.AppNotificationOrder
import com.example.foodapp.presentation.state.OrderEvent
import com.example.foodapp.presentation.state.OrderUiState
import com.example.foodapp.presentation.state.SuccessSharedState
import com.example.foodapp.presentation.state.setConversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
    private val chatRepository: ChatRepository,
) : ViewModel() {


    //  private val _cancelOrderEvent = MutableSharedFlow<UiState<Unit>>()
    //  tránh mất event khi UI chưa collect kịp.

    private val _orderUiState = MutableStateFlow(OrderUiState())
    val orderUiState = _orderUiState.asStateFlow()

    private val _event = MutableSharedFlow<OrderEvent>()
    val event = _event.asSharedFlow()

    private val _eventSuccess = MutableSharedFlow<SuccessSharedState>()
    val eventSuccess = _eventSuccess.asSharedFlow()
    private val handleOrderIds = mutableSetOf<String>()
    private val handleConversation = mutableSetOf<String>()
    private val handleDeactivate = mutableSetOf<String>()

    private val observingOrders = mutableSetOf<String>()


    init {
        observeOrders()
        observeOrderSuccess()
        Log.d("OrderFlow", "VM HASH = ${hashCode()}")


    }

    private fun observeOrder(orderId: String) {
        if (!observingOrders.add(orderId)) return //chưa tạo add chạy 1, lần 2 bỏ qua do đã add return

        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            orderRepository.observeOrderById(orderId).collectLatest { response ->

                when (response) {
                    is ApiResponse.Success -> {
                        if (response.data.status == OrderStatus.DELIVERED) {
//                            _orderUiState.update { it.copy(order = it.order.filter { item -> item.orderId == orderId }) }
                            _eventSuccess.emit(SuccessSharedState.DELIVERED)
//                            if (handleDeactivate.add(orderId)) {
//                                deactivateOrder(orderId)
//                            }
                        }
                        Log.d("checkVM_orderRepository", "success ${response.data}")
                        Log.d(
                            "checkVM_orderRepository",
                            "check dk 1 ${response.data.driverId != null} " +
                                    "check dk2 ${response.data.driverName != null} " +
                                    "check dk3 ${response.data.conversationId == null} " +
                                    "check dk4 ${orderId !in handleConversation}"
                        )

                        if (
                            response.data.driverId != null &&
                            response.data.driverName != null &&
                            response.data.conversationId == null &&
                            orderId !in handleConversation
                        ) {
                            handleConversation.add(orderId)
                            val chatResult = chatRepository.createConversation(
                                setConversation(
                                    customerId = userId,
                                    driverId = response.data.driverId,
                                    displayName = response.data.driverName
                                )
                            )

                            when (chatResult) {
                                is ApiResponse.Success -> {
                                    handleOrderIds.add(orderId)
                                    Log.d(
                                        "checkVM_createConversation",
                                        "success: ${chatResult.data}"
                                    )
                                    val updateResult = orderRepository.updateOrder(
                                        response.data.copy(
                                            conversationId = chatResult.data
                                        )
                                    )
                                    when (updateResult) {
                                        is ApiResponse.Success -> {
                                            Log.d("checkVM_updateResult", "success")
                                        }

                                        is ApiResponse.Error -> {
                                            Log.d(
                                                "checkVM_updateResult",
                                                "error ${updateResult.message}"
                                            )
                                        }

                                        else -> {
                                            Log.d("checkVM_updateResult", "else")
                                        }
                                    }
                                }

                                is ApiResponse.Error -> {
                                    Log.d(
                                        "checkVM_createConversation",
                                        "error: ${chatResult.message}"
                                    )
                                }

                                else -> {
                                    Log.d("checkVM_createConversation", "null")
                                }
                            }
                        }
                    }

                    is ApiResponse.Error -> {
                        Log.d("checkVM_orderRepository", "error ${response.message}")
                    }

                    else -> {
                        Log.d("checkVM_orderRepository", "else")
                    }
                }
            }
        }
    }




    private fun observeOrders() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            orderRepository.getOrderByUserId(userId).collectLatest { response ->
                Log.d("checkVM_observeOrder", "response = $response")
                when (response) {
                    is ApiResponse.Success -> {

                        Log.d("checkVM_observeOrder", "SUCESS = ${response.data}")
                        response.data.forEach { order ->
                            observeOrder(order.orderId)
                        }
                        _orderUiState.update { it.copy(order = response.data) }

                    }

                    is ApiResponse.Error -> {
                        Log.d("checkVM_observeOrder", "error = ${response.message}")
                    }

                    else -> {
                        Log.d("checkVM_observeOrder", "else")
                    }
                }
                Log.d("checkVM_observeOrder", "state = ${_orderUiState.value.order}")

            }
        }
    }

    //toi thay the bằng thay vì truyền thẳng, thì khi gọi hàm này tự xử lý để lấy dữ liệu luôn, ui khỏi cần truyền
//    fun placeOrder(cart: Cart, user: User, restaurant: Restaurant, paymentMethod: PaymentMethod) {
    fun placeOrder(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val user =
                (userRepository.getUserById(userId) as? ApiResponse.Success)?.data ?: return@launch
            val cart = (cartRepository.getCart(userId).first() as? ApiResponse.Success)?.data
                ?: return@launch
            val restaurant =
                (restaurantRepository.getRestaurantById(cart.restaurantId) as? ApiResponse.Success)?.data
                    ?: return@launch

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
                Log.d("checkVM_placeOrder", "success")

            } else if (result is ApiResponse.Error) {
                Log.d("checkVM_placeOrder order", "failed ${result.message}")
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

                        Log.d(
                            "checkVM_observeOrderSuccess",
                            "VM: check is Empty ${response.data} == ${response.data.isNotEmpty()}"
                        )
                        handleOrders.forEach {
                            handleOrderIds.add(it.orderId)
                        }
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
                                val response =
                                    notificationRepository.createNotification(notification = order.toAppNotification())

                                when (response) {
                                    is ApiResponse.Success -> {
                                        Log.d("check_VM_createNotification", "success")
                                    }

                                    is ApiResponse.Error -> {
                                        Log.d(
                                            "check_VM_createNotification",
                                            "error ${response.message}"
                                        )
                                    }

                                    else -> {
                                        Log.d("check_VM_createNotification", "else")
                                    }
                                }
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
                Log.d(
                    "checkVM_observeOrderSuccess",
                    " state: ${orderUiState.value.appNotificationOrder}"
                )
            }
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



