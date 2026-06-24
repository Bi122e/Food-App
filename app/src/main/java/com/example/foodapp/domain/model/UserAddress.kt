package com.example.foodapp.domain.model

 import com.google.firebase.firestore.ServerTimestamp
import java.util.Date


//dung api map de lay address, chua can
data class UserAddress(
    val addressId: String = "",
    val userId: String = "",
    val receiverName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val note: String = "",
    val isDefault: Boolean = false,

    @ServerTimestamp
    val createAt: Date? = null,
    @ServerTimestamp
    val updater: Date? = null,
)
