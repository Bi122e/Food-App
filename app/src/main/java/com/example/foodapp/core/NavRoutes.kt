package com.example.foodapp.core

import com.example.foodapp.domain.model.UserRole

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val RESISTER = "register"
    const val HOME = "home"

    const val COMPLETE_PROFILE = "complete_profile"
//    const val UserRoot = "user_root"
//    const val RestaurantRoot = "restaurant_root"
//    const val DriverRoot = "driver_root"
//    const val AdminRoot = "admin_root"

}

object UserRoutes {

     const val CHAT = "user_chat"
    const val PAYMENT = "user_payment"
    const val ORDER = "user_order"
    const val PREVIEW = "preview"
    const val HOME = "user_home"
    const val CART = "user_cart"
    const val PROFILE = "user_profile"
    const val INFO = "info"
    const val COMPLETE = "complete/{orderId}/{notificationId}"
    const val CHECKOUT = "checkout_home"

    const val EXPLORE_ROOT = "explore"
    const val EXPLORE = "explore/{mod}/{value}"
    const val SEARCH = "search"
    const val RESTAURANT = "restaurant/{restaurantId}"
    const val FOOD = "food/{foodId}"
    const val NOTIFICATION = "notification"

    fun completeDetail(orderId: String, notificationId: String): String {
        return "complete/$orderId/$notificationId"
    }
    fun restaurantDetail(restaurantId: String): String {
        return "restaurant/$restaurantId"
    }

    fun orderDetail(orderId: String): String {
        return "$ORDER/$orderId"
    }

    fun exploreTag(tag: String?) =
        "explore/tag/${tag}"

    fun exploreQuery(query: String) =
        "explore/query/${query}"
}


//fun UserRole.toRootRoute(): String = when (this) {
//    UserRole.CUSTOMER -> Routes.UserRoot
//    UserRole.RESTAURANT -> Routes.RestaurantRoot
//    UserRole.DRIVER -> Routes.DriverRoot
//    UserRole.ADMIN -> Routes.AdminRoot
//}


fun bottomRouteFromIndex(index: Int): String {
    return when (index) {
        0 -> UserRoutes.HOME
        1 -> UserRoutes.CHAT
        2 -> UserRoutes.CART
        3 -> UserRoutes.PROFILE
        else -> UserRoutes.HOME
    }
}


fun inRouteSnackBar(route: String): Boolean {

    return when (route) {
        Routes.COMPLETE_PROFILE -> false
        Routes.LOGIN -> false
        Routes.RESISTER -> false
        Routes.SPLASH -> false
        "${UserRoutes.ORDER}/{orderId}" -> false
        UserRoutes.PREVIEW -> false
        else -> true
    }
    //("${UserRoutes.ORDER}/{orderId}")
}


