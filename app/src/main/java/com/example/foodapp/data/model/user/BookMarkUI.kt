//file model bookmarkui
package com.example.foodapp.data.model.user

import com.example.foodapp.data.model.restaurant.Restaurant

data class FollowUI(
    val restaurantName: String,
    val restaurantImgUrl: String,
    val isFollow: Boolean = true,
)
fun BookMark.toUI(restaurant: Restaurant): FollowUI {
    return FollowUI(
        restaurantName = restaurant.restaurantName,
        restaurantImgUrl = restaurant.imageUrl,
        isFollow = true
    )
}
