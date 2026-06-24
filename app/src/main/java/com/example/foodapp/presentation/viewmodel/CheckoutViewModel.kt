package com.example.foodapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toUiState
import com.example.foodapp.core.utils.buildCartItemKey
import com.example.foodapp.data.repository.AuthRepository
import com.example.foodapp.data.repository.CartRepository
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.data.repository.ProfileRepository
import com.example.foodapp.data.repository.RestaurantRepository
import com.example.foodapp.data.repository.UserRepository
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.domain.model.User
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.presentation.state.CheckoutState
import com.example.foodapp.presentation.state.CheckoutUiState
import com.example.foodapp.presentation.state.OrderEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
     private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
     private val authRepository: AuthRepository,
    private val restaurantRepository: RestaurantRepository,
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

    private val _checkoutUiState = MutableStateFlow<CheckoutUiState>(CheckoutUiState())
    val checkoutUiState = _checkoutUiState.asStateFlow()

    private val _event = MutableSharedFlow<OrderEvent>()

    init {
        observeCart()
        observeUser()
    }

    // LOAD DATA
    private fun observeCart() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            cartRepository.getCart(userId = userId).collectLatest { response ->
                Log.d("CartState_getCart", "run:")
                when (response) {
                    is ApiResponse.Success -> {
                        Log.d("CartState_getCart", "Success")

                        val cart = response.data
                        _checkoutUiState.update {
                            it.copy(
                                cart = cart
                            )
                        }

                        val restaurantId = cart.restaurantId
                        getRestaurant(restaurantId = restaurantId)
                    }

                    is ApiResponse.Error -> {
                        Log.d("CartState_getCart", "Error")

                        _checkoutState.update {
                            it.copy(
                                error = response.message
                            )
                        }
                    }
                    is ApiResponse.Empty -> {
                        Log.d("CartState_getCart", "VM log empty")

                        _checkoutUiState.update {
                            it.copy(
                                cart = Cart(),

                            )
                        }
                        Log.d("CartState_getCart", "VM log empty: ${_checkoutUiState.value.cart}")
                        Log.d("CartState_getCart", "VM log empty: ${_checkoutUiState.value}")
                    }

                    else -> {
                        Log.d("CartState_getCart", ":")

                    }
                }
            }
        }
    }

    private fun getRestaurant(restaurantId: String) {
        viewModelScope.launch {

            val response = restaurantRepository.getRestaurantById(restaurantId)
                when (response) {
                    is ApiResponse.Error -> {
                        _checkoutUiState.update {
                            it.copy(
                                error = response.message
                            )
                        }
                    }

                    is ApiResponse.Success -> {
                        _checkoutUiState.update {
                            it.copy(
                                restaurant = response.data
                            )
                        }
                    }

                    else -> {}
                }
         }
    }

    private fun observeUser() {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            userRepository.getCurrentUser(userId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _checkoutUiState.update {
                            it.copy(
                                error = response.message
                            )
                        }
                        Log.d("CheckoutViewModel observeUser", response.message)
                    }

                    is ApiResponse.Success -> {
                        _checkoutUiState.update {
                            it.copy(
                                user = response.data
                            )
                        }
                        Log.d("CheckoutViewModel observeUser", "success ${response.data}")
                    }

                    else -> {}
                }
            }
        }
    }


    fun decreaseQty(foodId: String, variations: Map<String, List<VariationOption>>, quantity: Int) {
        viewModelScope.launch {
//            var currentQty = quantity
            var currentQty = quantity - 1
            Log.d("CheckFlowVM", currentQty.toString())

            val key = buildCartItemKey(
                foodId = foodId,
                variations = variations.mapValues { it.value.toSet() }
            )
            _checkoutUiState.update {
                it.copy(
                    loading = it.loading + key
                )
            }
            val userId = authRepository.currentUserId() ?: return@launch
//            if (quantity > 1) {
                currentQty -= 1
                val result = cartRepository.updateItemQuantity(
                    userId,
                    key = key,
                    quantity = currentQty
                )
                if (result is ApiResponse.Success) {
                    Log.d("CheckFlowVM", currentQty.toString())

                    Log.d("CheckFlowVM", "Success")
                    _checkoutUiState.update {
                        it.copy(
                            loading = it.loading - key
                        )
                    }
                }
                if (result is ApiResponse.Error) {
                    Log.d("CheckFlowVM", "Error ${result.message}")
                    _checkoutUiState.update {
                        it.copy(
                            error = result.message
                        )
                    }
                }
//            } else {
//                val result = cartRepository.removeItem(userId = userId, key = key)
//                    if (result is ApiResponse.Error) {
//                        _checkoutUiState.update {
//                            it.copy(
//                                error = result.message
//                            )
//                        }
//                    }
//            }
        }
    }

    fun selectPayment(paymentMethod: PaymentMethod) {
        _checkoutUiState.update { it.copy(paymentMethod = paymentMethod) }
        Log.d("PAYMENTMETHOD", _checkoutUiState.value.paymentMethod.toString())
    }
    fun increaseQty(foodId: String, variations: Map<String, List<VariationOption>>, quantity: Int) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId() ?: return@launch
            val key = buildCartItemKey(
                foodId = foodId,
                variations = variations.mapValues { it.value.toSet() })
            _checkoutUiState.update {
                it.copy(
                    loading = it.loading + key
                )
            }
            var currentQty = quantity
            if (quantity < 20) {
                currentQty += 1
            }
            val result = cartRepository.updateItemQuantity(
                userId = userId,
                key = key,
                quantity = currentQty
            )

            if (result is ApiResponse.Success) {
                _checkoutUiState.update {
                    it.copy(
                        loading = it.loading - key
                    )
                }
            }
            if (result is ApiResponse.Error) {
                _checkoutUiState.update {
                    it.copy(
                        error = result.message,
                        loading = it.loading - key
                    )
                }
            }
        }
    }


    fun updatePayment(paymentMethod: PaymentMethod) {
        _checkoutUiState.update {
            it.copy(paymentMethod = paymentMethod)
        }
    }

    fun loadCart(userId: String) {
        job?.cancel()
        job = viewModelScope.launch {
            cartRepository.getCart(userId).collect {
                _cart.value = it.toUiState()
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

    // VALIDATE

    private fun validate(): String? {
        val state = _checkoutState.value
        if (state.address.isBlank()) return "Thiếu địa chỉ"
        if (!state.phoneNumber.matches(Regex("0[0-9]{9}")))
            return "SĐT không hợp lệ"
        return null
    }


}