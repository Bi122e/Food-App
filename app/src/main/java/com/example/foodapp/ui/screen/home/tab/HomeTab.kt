package com.example.foodapp.ui.screen.home.tab


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
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
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.preview.PreviewData
import com.example.foodapp.ui.preview.PreviewData.categoryState
import com.example.foodapp.ui.preview.PreviewData.promotionState
import com.example.foodapp.ui.screen.home.section.CategorySelection
import com.example.foodapp.ui.screen.home.section.FeaturedFoodSelection
import com.example.foodapp.ui.screen.home.section.HeaderSection
import com.example.foodapp.ui.screen.home.section.PromotionSection
import com.example.foodapp.ui.screen.home.section.SearchSection
import com.example.foodapp.ui.theme.PrimaryBlue

//@Composable
//
//fun HomeTab(
//    address: String,
//    //            searchResult: UiState<List<Food>>,
////            headerHome: @Composable (String) -> Unit,
//    promotionState: UiState<List<Promotion>>,
//    profileState: ProfileUiState,
//    searchQueryState: String,
//    searchResultState: UiState<List<Food>>,
//    onQueryChange: (String) -> Unit,
//    categoryState: UiState<List<Category>>,
//    featuredFoodState: UiState<List<Food>>,
//    modifier: Modifier = Modifier,
//
//
//    ) {
////    var query by remember { mutableStateOf(query) }
////    var query by remember { mutableStateOf("") }
//
//    val focusManager = LocalFocusManager.current
////    val isSearchActive = query.isNotBlank()
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    0.0f to Color(0xff32c4e1),
//                    0.10f to PrimaryBlue,
//                    0.3f to Color(0xffebecee),
//                    0.7f to Color(0xfff8f9fb)
//                )
//            ),
//    ) {
//        LazyColumn(
//            modifier =
//                Modifier
//                    .fillMaxSize()
//                    .padding(20.dp)
//        ) {
//            item {
//                HeaderSection(address)
//            }
//            item {
//                Spacer(Modifier.height(20.dp))
//                SearchSection(
//                    query = searchQueryState,
//                    onValueChange =  onQueryChange,
//                    modifier = Modifier
//                )
//            }
//
//            item {
//                Spacer(Modifier.height(20.dp))
//                PromotionSection(promotionState = promotionState)
//            }
//
//            item {
//                Spacer(Modifier.height(20.dp))
//                CategorySelection(categoryState)
//            }
//
//            item {
//                Spacer(Modifier.height(20.dp))
//                FeaturedFoodSelection(featuredFoodState)
//            }
//
//        }
//    }
//}
@Composable
fun HomeTab(
    address: String,
    promotionState: UiState<List<Promotion>>,
//    profileState: ProfileUiState,
    searchQueryState: String,
    searchResultState: UiState<List<Food>>,
    onQueryChange: (String) -> Unit,
    categoryState: UiState<List<Category>>,
    featuredFoodState: UiState<List<Food>>,
    restaurantState: UiState<List<Restaurant>>,
    onClick: (foodId: String, restaurant: Restaurant, restaurantId: String) -> Unit
) {

    val focusManager = LocalFocusManager.current

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
            )
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {

            item {
                HeaderSection(address)
                Spacer(Modifier.height(20.dp))
            }

            item {
                SearchSection(
                    query = searchQueryState,
                    onValueChange = onQueryChange
                )
                Spacer(Modifier.height(20.dp))
            }

            item {
                PromotionSection(promotionState)
                Spacer(Modifier.height(20.dp))
            }

            item {
                CategorySelection(categoryState)
                Spacer(Modifier.height(20.dp))
            }

            item {
                FeaturedFoodSelection(
                    featuredFoodState,
                    restaurantState,
                    onClick = onClick)
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeTabPreview() {

        Scaffold(
            bottomBar = {
                HomeBottomBar(selectedIndex = 0, onItemSelected = {})
            }
        ) { padding ->

            HomeTab(
                address = "Cây Sốp",
                promotionState = PreviewData.promotionState,
//                profileState = ProfileUiState(),
                searchQueryState = "",
                searchResultState = PreviewData.foodState,
                onQueryChange = {},
                categoryState = PreviewData.categoryState,
                featuredFoodState = PreviewData.foodState,
                restaurantState = UiState.Loading,
                onClick = {_, _, _ ->}
            )
        }
    }
