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
    const val HOME = "user_home"
    const val CART = "user_cart"
    const val PROFILE = "user_profile"
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
