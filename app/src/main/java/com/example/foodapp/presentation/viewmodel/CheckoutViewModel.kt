package com.example.foodapp.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.mapper.toVariations
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderItem
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.domain.model.PaymentStatus
import com.example.foodapp.domain.model.User
import com.example.foodapp.presentation.state.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private var job: Job? = null

    private val _checkoutState = MutableStateFlow(CheckoutState())
    val checkoutState = _checkoutState.asStateFlow()

    private val _cart = MutableStateFlow<UiState<Cart>>(UiState.Loading)
    val cart = _cart.asStateFlow()

    private val _user = MutableStateFlow<UiState<User>>(UiState.Loading)
    val user = _user.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow(PaymentMethod.CASH)
    val selectedPaymentMethod = _selectedPaymentMethod.asStateFlow()

    // order result
    private val _orderResult = MutableSharedFlow<UiState<Order>>(extraBufferCapacity = 1)
    val orderResult = _orderResult.asSharedFlow()

    // LOAD DATA

    fun loadCart(userId: String) {
        job?.cancel()
        job = viewModelScope.launch {
            cartRepository.getCart(userId).collect {
                _cart.value = it.toUiState()
            }
        }
    }

    fun loadUser(userId: String) {
        job?.cancel()
        job = viewModelScope.launch {
            userRepository.getCurrentUser(userId).collect { response ->
                _user.value = response.toUiState()

                if (response is ApiResponse.Success) {
                    val profile = response.data.profile?.customer
                    if (profile != null) {
                        _checkoutState.update {
                            it.copy(
                                address = profile.address,
                                phoneNumber = profile.phone
                            )
                        }
                    }
                }
            }
        }
    }

    // UPDATE UI STATE

    fun updateAddress(address: String) {
        _checkoutState.update { it.copy(address = address) }
    }

    fun updatePhone(phone: String) {
        _checkoutState.update { it.copy(phoneNumber = phone) }
    }

    fun updateNotes(notes: String) {
        _checkoutState.update { it.copy(notes = notes) }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
    }

    // =========================
    // VALIDATE

    private fun validate(): String? {
        val state = _checkoutState.value
        if (state.address.isBlank()) return "Thiếu địa chỉ"
        if (!state.phoneNumber.matches(Regex("0[0-9]{9}")))
            return "SĐT không hợp lệ"
        return null
    }

    // =========================
    // PLACE ORDER

    fun placeOrder(userId: String) {
        viewModelScope.launch {

            val cartData = (_cart.value as? UiState.Success)?.data
            val userData = (_user.value as? UiState.Success)?.data

            if (cartData == null || userData == null) {
                _checkoutState.update { it.copy(error = "Thiếu dữ liệu") }
                return@launch
            }

            validate()?.let {
                _checkoutState.update { state -> state.copy(error = it) }
                return@launch
            }

            _checkoutState.update { it.copy(isProcessingOrder = true, error = null) }

            val order = Order(
                restaurantId = cartData.restaurantId,
                restaurantName = cartData.restaurantName,
                subTotal = cartData.calculateSubTotalPrice(),
                total = cartData.calculateTotalPrice() + cartData.deliveryFee,
                address = _checkoutState.value.address,
                phone = _checkoutState.value.phoneNumber,
                userId = cartData.userId,
                email = userData.email,
                paymentMethod = selectedPaymentMethod.value,
                paymentStatus = if (selectedPaymentMethod.value.isOnlinePayment)
                    PaymentStatus.PENDING else PaymentStatus.UNPAID,
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

            when (val res = orderRepository.createOrder(order)) {
                is ApiResponse.Success -> {
                    cartRepository.clearCart(userId)

                    _orderResult.emit(UiState.Success(order.copy(orderId = res.data)))

                    _checkoutState.update {
                        it.copy(isProcessingOrder = false)
                    }
                }

                is ApiResponse.Error -> {
                    _checkoutState.update {
                        it.copy(
                            isProcessingOrder = false,
                            error = res.message
                        )
                    }
                }

                else -> Unit
            }
        }
    }
}