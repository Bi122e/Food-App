package com.example.foodapp.ui.screen.home

import CartTab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.ProfileViewModel
import com.example.foodapp.presentation.viewmodel.PromotionViewModel
import com.example.foodapp.ui.screen.home.tab.ChatTab
import com.example.foodapp.ui.screen.home.tab.HomeTab
import com.example.foodapp.ui.screen.home.tab.ProfileTab


@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
//    promotionState: UiState<List<Promotion>>,
//    profileState: ProfileUiState
) {
    NavHost(
        navController = navController,
        startDestination = UserRoutes.HOME,
        modifier = modifier
    ) {
        composable(UserRoutes.HOME) {

            val promotionViewModel: PromotionViewModel = hiltViewModel()
            val profileViewModel: ProfileViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()

            val promotionState by promotionViewModel.uiState.collectAsStateWithLifecycle()
            val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
            val searchQueryState by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            val searchResultState by homeViewModel.searchResult.collectAsStateWithLifecycle()
            val addressState = profileState.editProfile.address


            HomeTab(
                promotionState = promotionState,
                profileState = profileState,
                searchQueryState = searchQueryState,
                searchResultState = searchResultState,
                address = addressState,
                onQueryChange = homeViewModel::updateSearchQuery,

                )
        }

        composable(UserRoutes.CHAT) { ChatTab() }
        composable(UserRoutes.CART) { CartTab() }
        composable(UserRoutes.PROFILE) { ProfileTab() }
    }
}