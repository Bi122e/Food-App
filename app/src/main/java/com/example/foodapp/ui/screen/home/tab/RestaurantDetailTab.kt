package com.example.foodapp.ui.screen.home.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.core.ApiResponse
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.UiStateHandler
import com.example.foodapp.domain.model.Favorite
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.ui.preview.PreviewDataFood
import com.example.foodapp.ui.preview.PreviewDataRestaurant
import com.example.foodapp.ui.preview.PreviewDataRestaurant.restaurantState
import com.example.foodapp.ui.screen.home.section.CartBottomBar
import com.example.foodapp.ui.screen.home.section.FoodItemCard
import com.example.foodapp.ui.screen.home.section.RestaurantHeaderSection
import com.example.foodapp.ui.screen.home.section.RestaurantTabRow
import com.example.foodapp.ui.theme.Pink0

@Composable
fun RestaurantDetailTab(
    restaurantState: UiState<Restaurant?>,
    foodsState: UiState<List<Food>?>,
    foodId: String? = null,
    favoriteState: UiState<Map<String, Favorite>>,
    onClickFavorite: (foodId: String) -> Unit,
    onClickBackHome: () -> Unit,
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val gribState = rememberLazyGridState()


    Scaffold(
        modifier = Modifier.fillMaxSize().background(Pink0),
        bottomBar = { CartBottomBar() }
    ) { _ ->

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(paddingValues)
        ) {
            //hEADER (COVER + AVATAR + INFO) ---
            UiStateHandler(uiState = restaurantState) { resData ->
                resData?.let { data ->
                    RestaurantHeaderSection(restaurant = data, onClickBackHome = onClickBackHome)
                }
            }

            Spacer(Modifier.height(16.dp))

            // tABS ---
            var selectedIndex by remember { mutableStateOf(0) }
            RestaurantTabRow(selectedIndex) { selectedIndex = it }

            Spacer(Modifier.height(16.dp))

            // list food
            when (selectedIndex) {
                0 -> {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Tất cả món ăn",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )


                    UiStateHandler(uiState = foodsState) { foodList ->
                        val displayList = if (isPreview) {
                            List(10) { PreviewDataFood.food }
                        } else {
                            foodList ?: emptyList()
                        }

                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(11.dp),
//                            contentPadding = PaddingValues(bottom = 0.dp)
                        ) {
                            val favorite =
                                (favoriteState as? UiState.Success<Map<String, Favorite>>)?.data ?: emptyMap()
                                    //true -> getData
                            items(displayList) { item ->
                                FoodItemCard(
                                    item = item,
                                    onClickFavorite = onClickFavorite,
                                    isFavorite = favorite.containsKey(item.foodId) )
                            }
                        }
                    }
                }
                1 -> { /* Popular Items logic */ }
                2 -> { /* Exclusive logic */ }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RestaurantTabPreview() {
    RestaurantDetailTab(
        foodsState = PreviewDataFood.foodState,
        restaurantState = PreviewDataRestaurant.restaurantState.data.let { UiState.Success(it.firstOrNull()) },
        foodId = "",
        onClickFavorite = {},
        favoriteState = UiState.Loading,
        onClickBackHome = {},
    )
}

