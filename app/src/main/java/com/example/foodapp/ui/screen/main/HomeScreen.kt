package com.example.foodapp.ui.screen.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.bottomRouteFromIndex
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.extentions.homeDataOrNull
import com.example.foodapp.presentation.extentions.isLoading
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.screen.shared.HomeTopBar
import com.example.foodapp.ui.screen.main.home.section.LoadingHomeTab

@SuppressLint("RestrictedApi")
@Composable
fun HomeScreen(
     userProfile: UserProfileViewModel = hiltViewModel()
) {

    val homeNavController = rememberNavController()
    val profileState by userProfile.uiStateProfile.collectAsStateWithLifecycle()

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeUiState by homeViewModel.homeUiState.collectAsStateWithLifecycle()


    val selectedIndex = remember(currentDestination) {
        when (currentDestination?.route) {
            UserRoutes.HOME -> 0
            UserRoutes.CONVERSATION -> 1
            UserRoutes.CART -> 2
            UserRoutes.PROFILE -> 3
            else -> 0
        }
    }



    LaunchedEffect(homeNavController) {

        homeNavController.currentBackStackEntryFlow.collect { entry ->
            try {
                val routes =
                    homeNavController.currentBackStack.value.mapNotNull { it.destination.route }
                Log.d("NavigationLog", "Home BackStack: ${routes.joinToString(" -> ")}")
            } catch (e: Exception) {
                Log.d("NavigationLog", "Current route: ${entry.destination.route}")
            }
        }
    }


    val bottomBarRoute = listOf(
        UserRoutes.HOME, UserRoutes.CONVERSATION,  UserRoutes.PROFILE

        )
    val isShowBottomBar = currentDestination?.route in bottomBarRoute
    val isShowTopBar = currentDestination?.route == UserRoutes.HOME

    val homeData = homeUiState.homeDataOrNull
    val listState = rememberLazyListState()
    val collapsed by remember {
        derivedStateOf {
            listState.firstVisibleItemScrollOffset > 50 || listState.firstVisibleItemIndex > 0
        }
    }
     when {

        homeUiState.isLoading -> {
            Log.d("HomeScreen", "State: LOADING")

            LoadingHomeTab()
        }
        homeData != null -> {

            Log.d("HomeScreen", "State: SUCCESS, homeData=$homeData")
            Log.d("scroll list state", "visible item scroll = ${listState.firstVisibleItemScrollOffset}")

            Scaffold(
                topBar = {
                    if (isShowTopBar) {
                        HomeTopBar(
                            collapsed = collapsed,
                            onNavigationToSearchTab = {
                                homeNavController.navigate(UserRoutes.EXPLORE_ROOT)
                            },
                            badgeCount = homeUiState.badgeCount,
                            onNavigationToNotification = { homeNavController.navigate(UserRoutes.NOTIFICATION) }
                        )
                    }
                },
                bottomBar = {

                    if (isShowBottomBar) {

                        HomeBottomBar(

                            selectedIndex = selectedIndex, //lấy idx hiện tại để so sánh với idx UI (rồi mới dòng này)
                            badgeProfile = profileState.profileCompleteness == ProfileCompleteness.INCOMPLETE,
                            onItemSelected = { index -> //gán idx  (chạy dòng này trước)
                                //tạo idx có sẵn, user click cái nào = bằng cái đó
                                val route = bottomRouteFromIndex(index) //đổi index sang route /home - chat,...
                                //nav sang route mà idx vừa gán
                                homeNavController.navigate(route) {
                                    popUpTo(homeNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }

            ) { paddingValues ->


                 Log.d("NavigationLog", "Home run")
                HomeNavGraph(
                    listState = listState,
                    navController = homeNavController,
                    padding = paddingValues,
                    homeViewModel = homeViewModel,
                    homeUiState = homeUiState,
                    homeData = homeData
                )
            }
        }

        else -> {
            Log.e("HomeScreen", "State: EMPTY/ERROR — homeUiState=$homeUiState")
             LoadingHomeTab()
        }

        }
    }



