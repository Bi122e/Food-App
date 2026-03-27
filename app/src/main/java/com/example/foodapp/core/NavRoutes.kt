package com.example.foodapp.core

import com.example.foodapp.domain.model.UserRole

object Routes {
    const val Splash = "splash"
    const val Login = "login"
    const val Register = "register"
    const val UserRoot = "user_root"
    const val RestaurantRoot = "restaurant_root"
    const val DriverRoot = "driver_root"
    const val AdminRoot = "admin_root"

}

object UserRoutes {
    const val CHAT = "user_chat"
    const val CHAT_ROOT = "chat_root"
    const val HOME = "user_home"
    const val HOME_ROOT = "home_root"
    const val CART = "user_cart"
    const val CART_ROOT = "cart_root"
    const val PROFILE = "user_profile"
    const val PROFILE_ROOT = "profile_root"

    const val RESTAURANT = "user_restaurant"
    const val RESTAURANT_ROOT = "restaurant_root"
    //RESTAURANTDETAIL RESTAURANT DETAIL


}

fun UserRole.toRootRoute(): String = when (this) {
    UserRole.CUSTOMER -> Routes.UserRoot
    UserRole.RESTAURANT -> Routes.RestaurantRoot
    UserRole.DRIVER -> Routes.DriverRoot
    UserRole.ADMIN -> Routes.AdminRoot
}


fun bottomRouteFromIndex(index: Int): String {
    return when(index) {
        0 -> UserRoutes.HOME
        1 -> UserRoutes.CHAT
        2 -> UserRoutes.CART
        3 -> UserRoutes.PROFILE
        else -> UserRoutes.HOME
    }
}

fun isShowBottomBar(route: String): Boolean {
    return when(route) {
        UserRoutes.HOME,
        UserRoutes.CHAT,
        UserRoutes.CART,
        UserRoutes.PROFILE -> true
        else -> false
    }
}

