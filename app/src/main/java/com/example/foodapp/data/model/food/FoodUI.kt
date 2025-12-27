////file model foodui
//package com.example.foodapp.data.model.food
//
//import com.example.foodapp.data.model.restaurant.Restaurant
//import com.example.foodapp.domain.Food
//import com.example.foodapp.domain.Variation
//import kotlin.String
//
//data class FoodUI(
//    val foodImgUrl: String,
//    val foodTime: Int,
//    val foodId: String,
//    val restaurantId: String,
//    val foodName: String,
//    val restaurantName: String,
//    val deliveryFree: Int,
//    val reviews: Int,
//    val totalRating: Double,
//    val price: Double,
//    val isAvailable: Boolean,
////    val isFavorite: Boolean,
//    val ingredient: String,
//    val calories: Int,
//    val variation: List<Variation>,
//)
//
//fun Food.toFoodUI(
////    isFavorite: Boolean,
//    restaurant: Restaurant,
//): FoodUI {
//    return FoodUI(
//        foodImgUrl = imageUrl,
//        foodTime = foodTime,
//        foodName = name,
//        restaurantName = restaurant.restaurantName,
//        deliveryFree = restaurant.deliveryFree,
//        reviews = reviews,
//        totalRating = getAverageReviews(),
//        price = price,
//        isAvailable = isAvailable,
////        isFavorite = isFavorite,
//        ingredient = ingredient,
//        calories = calories,
//        variation = variation,
//        restaurantId = restaurant.restaurantId,
//        foodId = foodId
//    )
//}
