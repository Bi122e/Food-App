//file model restaurantUI
package com.example.foodapp.data.model.restaurant

data class RestaurantUI(

    val restaurantName: String,
    val restaurantImgUrl: String,
    val restaurantReviews: Int,
    val restaurantTotalRating: Double,
    val description: String,
    val isOpen: Boolean,
    val email: String,
    val phoneNumber: String,
    val address: String,

    )

fun Restaurant.toUI(): RestaurantUI {
    return RestaurantUI(
        restaurantName = restaurantName,
        restaurantImgUrl = imageUrl,
        restaurantReviews = review,
        restaurantTotalRating = getAverageRating(),
        description = description,
        isOpen = isOpen,
        email = email,
        phoneNumber = phoneNumber,
        address = address,


    )
}
