package com.example.foodapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.mapper.toVariations
import com.example.foodapp.domain.model.*
import com.example.foodapp.presentation.state.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val job: Job? = null
) : ViewModel() {


    private val _checkoutState = MutableStateFlow(CheckoutState())
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    private val _cart = MutableStateFlow<UiState<Cart>>(UiState.Loading)
    val cart: StateFlow<UiState<Cart>> = _cart.asStateFlow()

    private val _user = MutableStateFlow<UiState<User>>(UiState.Loading)
    val user: StateFlow<UiState<User>> = _user.asStateFlow()

    private val _order = MutableStateFlow<UiState<Order>>(UiState.Loading)
    val order: StateFlow<UiState<Order>> = _order.asStateFlow()


    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.CASH)
    val selectedPaymentMethod: StateFlow<PaymentMethod> =
        _selectedPaymentMethod.asStateFlow()


    fun loadCart(userId: String) {
        job?.cancel()

        viewModelScope.launch {
            cartRepository.getCart(userId)
                .collect { response ->
                    _cart.value = response.toUiState()
                }
        }
    }

    fun loadUser(userId: String) {
        job?.cancel()
        viewModelScope.launch {
            userRepository.getCurrentUser(userId)
                .collect { response ->
                    _user.value = response.toUiState()

                    if (response is ApiResponse.Success) {
                        _checkoutState.update { it.copy(address = response.data.address) }
                        _checkoutState.update { it.copy(phoneNumber = response.data.phone) }
                    }
                }
        }
    }

    fun loadOrder(orderId: String) {
        viewModelScope.launch {
            _order.value = UiState.Loading
            val response = orderRepository.getOrderById(orderId)
            _order.value = response.toUiState()
        }
    }


    fun updateDeliveryAddress(address: String) {
        _checkoutState.update { it.copy(address = address) }
    }

    fun updatePhoneNumber(phone: String) {
        _checkoutState.update { it.copy(phoneNumber = phone) }
    }

    fun updateNotes(notes: String) {
        _checkoutState.update { it.copy(notes = notes) }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }


    private fun calculateTotals(cart: Cart): CheckoutTotals {
        val subTotal = cart.calculateSubTotalPrice()
        val deliveryFee = 3000
        val discount = 0
        val total = subTotal + deliveryFee - discount

        return CheckoutTotals(
            subTotal = subTotal,
            deliveryFee = deliveryFee,
            discount = discount,
            total = total
        )
    }


    fun placeOrder(userId: String) {

        viewModelScope.launch {
            val cartData = (_cart.value as? UiState.Success)?.data
            val userData = (_user.value as? UiState.Success)?.data

            if (cartData == null || userData == null) {
                _checkoutState.update {
                    it.copy(
                        error = "Khong tim thay du lieu"
                    )
                }
                return@launch
            }

            if (_checkoutState.value.address.isBlank() ||
                _checkoutState.value.phoneNumber.length <= 10
            ) {
                _checkoutState.update {
                    it.copy(
                        error = "Thong tin khong hop le"
                    )
                }
                return@launch
            }

            _checkoutState.update {
                it.copy(
                    isProcessingOrder = true, error = null
                )
            }

            val order = Order(
                restaurantId = cartData.restaurantId,
                restaurantName = cartData.restaurantName,
                subTotal = cartData.calculateSubTotalPrice(),
                total = cartData.calculateTotalPrice() + cartData.deliveryFee,
                address = _checkoutState.value.address,
                phone = _checkoutState.value.phoneNumber,
                userId = cartData.userId,
                orderId = "",
                email = userData.email,
                items = cartData.cartItems.map { item ->
                    OrderItem(
                        foodId = item.foodId,
                        foodName = item.name,
                        variations = item.variation.toVariations(),
                        selectedOptions = item.variation.mapValues { it.value.toList() },
                        imgUrl = item.imgUrls,
                        notes = item.notes,
                        price = item.price,
                        quantity = item.quantity
                    )
                }
            )

            when (val response = orderRepository.createOrder(order)) {
                is ApiResponse.Success -> {
                    cartRepository.clearCart(userId)
                    _checkoutState.update {
                        it.copy(
                            isProcessingOrder = false
                        )
                    }
                }

                is ApiResponse.Error -> {
                    _checkoutState.update {
                        it.copy(
                            isProcessingOrder = false,
                            error = response.message
                        )
                    }
                }

                else -> Unit
            }

//            if (!validateCheckoutInfo()) {
//                _checkoutState.value =
//                    CheckoutState.Error("Vui lòng điền đầy đủ thông tin")
//                return@launch
//            }

        }
    }


//    private fun validateCheckoutInfo(): Boolean {
//        return _deliveryAddress.value.isNotBlank() &&
//                _phoneNumber.value.length >= 10
//    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState()
    }

    fun resetError() {
        _checkoutState.update {
            it.copy(error = null)
        }
    }
}



data class CheckoutTotals(
    val subTotal: Int = 0,
    val deliveryFee: Int = 0,
    val discount: Int = 0,
    val total: Int = 0
)

