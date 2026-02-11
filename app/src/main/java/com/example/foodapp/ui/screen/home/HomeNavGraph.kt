package com.example.foodapp.ui.screen.home

import CartTab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.ui.screen.home.tab.ChatTab
import com.example.foodapp.ui.screen.home.tab.HomeTab
import com.example.foodapp.ui.screen.home.tab.ProfileTab


@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = UserRoutes.HOME,
        modifier = modifier
    ) {
        composable(UserRoutes.HOME) { HomeTab() }
        composable(UserRoutes.CHAT) { ChatTab() }
        composable(UserRoutes.CART) { CartTab() }
        composable(UserRoutes.PROFILE) { ProfileTab() }
    }
}