package com.example.foodapp.ui.screen.navigation

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.ui.screen.login.LoginScreen
import com.example.foodapp.ui.screen.register.RegisterScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient

//@Composable
//fun AppNavGraph(
//    viewModel: AuthViewModel,
//    googleLauncher: ActivityResultLauncher<Intent>,
//    googleClient: GoogleSignInClient
//) {
//    val navController = rememberNavController()
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    NavHost(
//        navController = navController,
//        startDestination = "login"
//    ){
//
//        composable("login") {
//            LoginScreen(
//                navController = navController,
//                uiState = uiState,
//                onLoginClick = viewModel::login,
//                onGoogleLogin = {googleLauncher.launch(googleClient.signInIntent)},
//                onRegisterClick = {
//                    navController.navigate("register")
//                }
//            )
//        }
//
//        composable("register") {
//            Text("Register Screen")
//        }
//
//        composable("home") {
//            Text("Home Screen")
//        }
//    } }

@Composable
fun AppNavGraph(
    viewModel: AuthViewModel,
    googleLauncher: ActivityResultLauncher<Intent>,
    googleClient: GoogleSignInClient
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    NavHost(startDestination = "login", navController = navController) {
        composable("login") {
            LoginScreen(
                navController = navController,
                uiState = uiState,
                onGoogleLogin = {googleLauncher.launch(googleClient.signInIntent)},
                onLoginClick = viewModel::login,
                onRegisterClick = {
                    viewModel.resetState()
                    navController.navigate("register")
                        // LoginScreen -> co the thử cách này, reset trong UI
//                        onRegisterClick = {
//                    viewModel.resetState()
//                    navController.navigate("register")
                }
            )
        }

        composable("register"){
                RegisterScreen(
                    navController = navController,
                    uiState = uiState,
                    onRegisterClick = viewModel::register,
                    onResetState = viewModel::resetState
                )
            }

        composable("home") {  }

    }

}