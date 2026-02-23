package com.example.foodapp.ui.screen.home.tab


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Promotion
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.screen.home.section.CategorySelection
import com.example.foodapp.ui.screen.home.section.HeaderSection
import com.example.foodapp.ui.screen.home.section.PromotionSection
import com.example.foodapp.ui.screen.home.section.SearchSection
import com.example.foodapp.ui.theme.PrimaryBlue

@Composable

fun HomeTab(
    address: String,
    //            searchResult: UiState<List<Food>>,
//            headerHome: @Composable (String) -> Unit,
    promotionState: UiState<List<Promotion>>,
    profileState: ProfileUiState,
    searchQueryState: String,
    searchResultState: UiState<List<Food>>,
    onQueryChange: (String) -> Unit,
    categoryState: UiState<List<Category>>

    ) {
//    var query by remember { mutableStateOf(query) }
//    var query by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
//    val isSearchActive = query.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0.0f to Color(0xff32c4e1),
                    0.10f to PrimaryBlue,
                    0.3f to Color(0xffebecee),
                    0.7f to Color(0xfff8f9fb)
                )
            ),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
        ) {
            item {
                HeaderSection(address)
            }
            item {
                Spacer(Modifier.height(20.dp))
                SearchSection(
                    query = searchQueryState,
                    onValueChange = { onQueryChange(searchQueryState) },
                    modifier = Modifier
                )
            }

            item {
                Spacer(Modifier.height(20.dp))
                PromotionSection(promotionState = promotionState)
            }

            item {
                Spacer(Modifier.height(20.dp))
                CategorySelection(categoryState)
            }

        }
    }
}

@Preview
@Composable
fun HomeTabPreview() {
    HomeTab(
        address = "No Na",
        promotionState = UiState.Success(emptyList()),
        profileState = ProfileUiState(),
        searchQueryState = "",
        searchResultState = UiState.Success(emptyList()),
        onQueryChange = {},
        categoryState = UiState.Success(emptyList())
    )
}