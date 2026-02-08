package com.example.foodapp.ui.screen.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.AuthResult
import com.example.foodapp.core.Routes
import com.example.foodapp.core.UiState
import com.example.foodapp.core.toRootRoute
import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.ui.screen.home.HomeScreen
import com.example.foodapp.ui.screen.login.LoginScreen
import com.example.foodapp.ui.screen.register.RegisterScreen
import com.example.foodapp.ui.screen.splash.SplashScreen

@Composable
fun AppNavGraph(
    startDestination: String,
    appState: AppState,
    uiState: UiState<AuthResult>,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onLogout: () -> Unit,
    onResetState: () -> Unit
) {
    val navController = rememberNavController()

    // Handle post-launch navigation (Login success, etc.)
    LaunchedEffect(appState) {
        if (appState is AppState.LoggedIn) {
            val route = appState.user.role.toRootRoute()
            Log.d("AppNavGraph", "AppState changed to LoggedIn. Navigating to $route")
            navController.navigate(route) {
                popUpTo(0) { inclusive = true }
            }
        } else if (appState is AppState.Guest) {
            Log.d("AppNavGraph", "AppState changed to Guest. Navigating to Login")
            navController.navigate(Routes.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Routes.Splash) {
                SplashScreen()
            }

            composable(Routes.Login) {
                LoginScreen(
                    uiState = uiState,
                    onLoginClick = onLoginClick,
                    onGoogleLogin = onGoogleLogin,
                    onRegisterClick = {
                        onResetState()
                        navController.navigate(Routes.Register)
                    }
                )
            }

            composable(Routes.Register) {
                RegisterScreen(
                    uiState = uiState,
                    onRegisterClick = onRegisterClick,
                    onResetState = onResetState,
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }


            composable(Routes.UserRoot) {
//                RoleRootScreen(roleName = "User", onLogout = {
//                    onLogout()
//                }
//                )
                HomeScreen()
            }

            composable(Routes.RestaurantRoot) {
                RoleRootScreen(roleName = "Restaurant", onLogout = {
                    onLogout()
                })
            }

            composable(Routes.DriverRoot) {
                RoleRootScreen(roleName = "Driver", onLogout = {
                    onLogout()
                })
            }

            composable(Routes.AdminRoot) {
                RoleRootScreen(roleName = "Admin/Manager", onLogout = {
                    onLogout()
                })
            }
        }
    }
}

private fun performLogout(navController: NavHostController, onLogout: () -> Unit) {
    onLogout()
    navController.navigate(Routes.Login) {
        popUpTo(0) { inclusive = true }
    }
}

@Composable
fun RoleRootScreen(roleName: String, onLogout: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to $roleName Root 🎉")
        Button(onClick = {
            Log.d("Navigation", "Logging out from $roleName")
            Toast.makeText(context, "Logging out...", Toast.LENGTH_SHORT).show()
            onLogout()
        }) {
            Text("Logout")
        }
    }
}
