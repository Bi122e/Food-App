package com.example.foodapp.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapp.core.UiState
import com.example.foodapp.data.repository.OrderRepository
import com.example.foodapp.domain.model.PaymentStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _paymentState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val paymentState = _paymentState.asStateFlow()

    fun processPayment(orderId: String) {
        viewModelScope.launch {
            _paymentState.value = UiState.Loading

            // giả lập payment
            val success = true

            if (success) {
                orderRepository.updatePaymentStatus(orderId, PaymentStatus.PAID)
                _paymentState.value = UiState.Success(Unit)
            } else {
                orderRepository.updatePaymentStatus(orderId, PaymentStatus.FAILED)
                _paymentState.value = UiState.Error("Thanh toán thất bại")
            }
        }
    }

    fun reset() {
        _paymentState.value = UiState.Idle
    }

}