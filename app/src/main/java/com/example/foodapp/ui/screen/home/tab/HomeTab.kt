package com.example.foodapp.ui.screen.home.tab


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodapp.core.UiState
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.ProfileViewModel
import com.example.foodapp.ui.screen.home.HeaderHome
import com.example.foodapp.ui.screen.home.HomeTabContent

@Composable
fun HomeTab(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()

) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val address = uiState.editProfile.address

    val query by homeViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResult by homeViewModel.searchResult.collectAsStateWithLifecycle()

    HomeTabContent(
        address,
        query = query,
        searchResult = searchResult,
        headerHome = {it -> HeaderHome(it)}
    ) { query ->
        homeViewModel.updateSearchQuery(query)
    }
}



@Preview
@Composable
fun HomeTabPreview() {
    HomeTabContent(
        "", "", UiState.Success(emptyList()), {}, {})
}