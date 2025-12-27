//file model userUI
package com.example.foodapp.data.model.user

data class UserUI(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val profileImageUrl: String,
    val address: String,

)
fun User.toUI(): UserUI {
    return UserUI(
        name = name,
        email = email,
        phoneNumber = phoneNumber,
        profileImageUrl = profileImageUrl,
        address = address
    )
}
