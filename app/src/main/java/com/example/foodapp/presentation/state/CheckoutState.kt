package com.example.foodapp.presentation.state

import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.PaymentMethod
//UI Form State
data class CheckoutState(

    //user
    val userName: String = "",
    val address: String = "",
    val phoneNumber: String = "",
    val email: String = "",

    //restaurant
    val restaurantName: String = "",

    //cart
    val cart: Cart? = null,

    //note
    val notes: String = "",

    //payment
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val promoCode: String = "",
    val discountAmount: Int = 0,
    val isPromoCodeValid: Boolean = false,
    //Ui State
    val isLoading: Boolean = false,
    val isProcessingOrder: Boolean = false,
    val error: String? = null
    ) {

    fun getSubTotal(): Int = cart?.calculateSubTotalPrice()?: 0

    fun getDeliveryFee(): Int = cart?.deliveryFee ?: 0

    fun getTotalAmount(): Int {
        return (getTotalAmount() + getDeliveryFee() - discountAmount).coerceAtLeast(0)
    }

    fun isPhoneNumberValid(): Boolean {
        if (phoneNumber.isEmpty()) return false
        return phoneNumber.matches(Regex("^(0[3|5|7|8|9])+([0-9]{8})$"))    }

    fun isEmailValid(): Boolean {
        if (email.isEmpty()) return true // Email is optional
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
    }

    fun isValid(): Boolean {
        return userName.isNotEmpty() &&
                address.isNotEmpty() &&
                isPhoneNumberValid() &&
                isEmailValid() &&
                cart?.isValid() == true
    }

    fun canCheckout(): Boolean {
        return isValid() &&
                cart?.isEmpty() == false &&
                !isLoading &&
                !isProcessingOrder &&
                getTotalAmount() > 0

    }

    fun getValidationError(): String ? {
        return when {
            userName.isEmpty() -> "Vui long nhap ten nguoi dung"
            phoneNumber.isEmpty() -> "Vui long nhap so dien thoai"
            !isPhoneNumberValid() -> "So dien thoai khong hop le"
            address.isEmpty() -> "Vui long nhap dia chi giao hang"
            email.isEmpty() -> "Vui long nhap dia chi email"
            isEmailValid() -> "Vui long nhap dia chi email hop le"
            cart?.isEmpty() == true -> "Gio hang trong"
            getTotalAmount() <= 0 -> "So tien khong hop le"
            else -> null
        }
    }
}