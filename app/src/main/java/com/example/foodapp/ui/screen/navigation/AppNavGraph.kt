package com.example.foodapp.ui.screen.navigation

import com.example.foodapp.ui.screen.main.HomeScreen
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.Routes
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.User
import com.example.foodapp.presentation.state.AppState
import com.example.foodapp.presentation.viewmodel.OrderViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.screen.initializationInfo.CompleteProfileScreen
import com.example.foodapp.ui.screen.initializationInfo.DialogProgress
import com.example.foodapp.ui.screen.login.LoginScreen
import com.example.foodapp.ui.screen.register.RegisterScreen
 import com.example.foodapp.ui.screen.splash.SplashScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi", "ContextCastToActivity")
@Composable
fun AppNavGraph(
    startDestination: String,
    appState: AppState,
//    uiState: UiState<AuthResult>,
    onLoginClick: (String, String) -> Unit,
    onRegisterClick: (String, String) -> Unit,
    onGoogleLogin: () -> Unit,
    onLogout: () -> Unit,
    onResetState: () -> Unit,
    onLoggedIn: (updateUser: User) -> Unit,

    ) {

    Log.d("AppNavGraph", "START DES: $startDestination")

    val activity = LocalActivity.current as ComponentActivity
    val navController = rememberNavController()
    val orderViewModel: OrderViewModel = hiltViewModel(activity)
    val orderState = orderViewModel.orderUiState.collectAsStateWithLifecycle()


    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { entry ->
            try {
                val routes =
                    navController.currentBackStack.value.mapNotNull { it.destination.route }
                Log.d("NavigationLog", "App BackStack: ${routes.joinToString(" -> ")}")
            } catch (e: Exception) {
                Log.d("NavigationLog", "App Current destination: ${entry.destination.route}")
            }
        }
    }

    // check key co thay doi ko, neu co thi chay side effect de tranh state thay doi compose recomposition nhieu lan
    LaunchedEffect(appState) {
        when (appState) {
            is AppState.Loading -> {
                navController.navigate(Routes.SPLASH)
                Log.d("AppNavGraph", "Loading")
            }

            is AppState.Guest -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
                Log.d("AppNavGraph", "LOGIN")

            }

            is AppState.LoggedIn -> {
                navController.navigate(Routes.HOME) {
                    popUpTo(0) { inclusive = true }
                }
                Log.d("AppNavGraph", "HOME")

            }

            is AppState.NeedCompleteProfile -> {
                navController.navigate(Routes.COMPLETE_PROFILE) {
                    popUpTo(0) { inclusive = true }
                }
                Log.d("AppNavGraph", "COMPLETE")

            }

            else -> {
                Log.d("AppNavGraph", "ELSE BRANCH")

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
            composable(Routes.SPLASH) {
                SplashScreen()
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    appState = appState,
                    onLoginClick = onLoginClick,
                    onGoogleLogin = onGoogleLogin,
                    onRegisterClick = {
                        onResetState()
                        navController.navigate(Routes.RESISTER)
                    }
                )
            }

            composable(Routes.RESISTER) {
                RegisterScreen(
                    appState = appState,
                    onRegisterClick = onRegisterClick,
                    onResetState = onResetState,
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Routes.COMPLETE_PROFILE) {
                val userProfileViewModel: UserProfileViewModel = hiltViewModel()
                val profileUiState by userProfileViewModel.uiStateProfile.collectAsState()


                CompleteProfileScreen(
                    onLoggedIn = onLoggedIn,
                    updateUserProfile = {
                        Log.d("ApiResponse Run", "dc goi")
                        userProfileViewModel.updateUserProfile()
                    },
                    onFieldEditProfileChange = { field, value ->
                        Log.d("ApiResponse Run", "onFIeld, $field $value")
                        userProfileViewModel.onFieldEditProfileChange(field, value)
                    },
                    profileUiState = profileUiState,
                    onNextStep = {
                        userProfileViewModel.nextStep()
                    },
                    onPrevious = {
                        userProfileViewModel.previousStep()
                    },
                    validatePhone = userProfileViewModel::validatePhone,
                    validateName = userProfileViewModel::validateName,
                    resetClickedUpdate = userProfileViewModel::resetClickedUpdate,
                    setClickedUpdate = userProfileViewModel::setClickedUpdate,
                    setGender = {
                        userProfileViewModel.setGender(it)
                    }
                )
            }

            composable(route = Routes.HOME) {
                HomeScreen()
            }
        }

        val activity = (LocalContext.current as Activity) //finished de exit chuong trinh`
        var showExitDialog by remember { mutableStateOf(false) }


        //handle lai back
        BackHandler() {
            /* hoac cach viet 2,

          * if (!navController.getBackStack())
          *   showDialog = true
          *
          * */

            val currentRoute = navController.currentDestination?.route
            when {
                currentRoute == Routes.COMPLETE_PROFILE -> {
                    showExitDialog = true
                }

                else -> navController.popBackStack()
            }
        }

        if (showExitDialog) {
            DialogProgress(
                onCloseDialog = {
                    showExitDialog = false
                },
                onExitProgram = {
                    activity.finish()
                }
            )
        }
    }
}





