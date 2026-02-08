package com.example.foodapp.ui.screen.navigation

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

fun UserRole.toRootRoute(): String = when (this) {
    UserRole.CUSTOMER -> Routes.UserRoot
    UserRole.RESTAURANT -> Routes.RestaurantRoot
    UserRole.DRIVER -> Routes.DriverRoot
    UserRole.ADMIN -> Routes.AdminRoot
}
