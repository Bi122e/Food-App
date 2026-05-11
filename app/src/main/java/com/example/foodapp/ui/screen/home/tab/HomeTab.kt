package com.example.foodapp.ui.screen.home.tab


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Promotion
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.extensions.pulseSkeleton
import com.example.foodapp.presentation.state.OrderUiState
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.preview.PreviewData
import com.example.foodapp.ui.screen.home.section.CategorySelection
import com.example.foodapp.ui.screen.home.section.FeaturedFoodSelection
import com.example.foodapp.ui.screen.home.section.HeaderSection
import com.example.foodapp.ui.screen.home.section.PromotionSection
import com.example.foodapp.ui.screen.home.section.SearchSection
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.PrimaryBlue

@Composable
fun HomeTab(
    modifier: Modifier? = null,
    address: String,
    paddingValues: PaddingValues,
    promotionState: UiState<List<Promotion>>,
//    profileState: ProfileUiState,
    searchQueryState: String,
    searchResultState: UiState<List<Food>>,
    onQueryChange: (String) -> Unit,
    categoryState: UiState<List<Category>>,
    featuredFoodState: UiState<List<Food>>,
    restaurantState: UiState<List<Restaurant>>,
    onClick: (foodId: String, restaurant: Restaurant, restaurantId: String) -> Unit,
    orderUiState: OrderUiState,
) {


    val focusManager = LocalFocusManager.current
    val order = orderUiState.order


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

        val composition by rememberLottieComposition(
            //app/src/main/assets/making_food.json
            LottieCompositionSpec.Asset("making_food.json")
        )
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        val context = LocalContext.current

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = paddingValues
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
            item {


//                var loading by remember {
//                    mutableStateOf(true)
//                }
//                Crossfade(
//                    targetState = loading,
//                    animationSpec = tween(300),
//                    label = ""
//                ) { _: Boolean ->
//                    Skeleton()
//                }
                Skeleton()
            }
        }




        //circle order
        if (order.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-100).dp)
                    .size(60.dp)
                    .clickable(
                        onClick = {
                            showToast(context, "clicked")
                        }
                    )
                    .background(
                        brush = Brush.radialGradient(
//                        colors = listOf(Gray65.copy(alpha = 0.7f), Blue1)
                            colors = listOf(
                                Color.White, Blue1.copy(alpha = 0.3f)
                            )

                        ),
                        CircleShape
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center

            ) {
                Icon(
                    imageVector = Icons.Rounded.Circle,
                    contentDescription = null,
                    tint = Color.Red.copy(
                        alpha = 0.5f
                    ),
                    modifier = Modifier
                        .offset(x = (18).dp, y = (-12).dp)
                        .size(12.dp)

                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress},
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }



//        Box(
//            modifier = Modifier
//                .size(100.dp)
//                .offset(y = (-100).dp, x = 16.dp)
//                .background(Color.Red, CircleShape)
//                .align(Alignment.BottomStart)
//        ) {
//
//        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeTabPreview() {

        Scaffold(
            bottomBar = {
                HomeBottomBar(selectedIndex = 0, onItemSelected = {}, false)
            }


        ) { paddingValues ->

            HomeTab(
                modifier = Modifier.padding(paddingValues),
                address = "Cây Sốp",
                promotionState = PreviewData.promotionState,
//                profileState = ProfileUiState(),
                searchQueryState = "",
                searchResultState = PreviewData.foodState,
                onQueryChange = {},
                categoryState = PreviewData.categoryState,
                featuredFoodState = PreviewData.foodState,
                restaurantState = UiState.Loading,
                onClick = {_, _, _ ->},
                paddingValues = PaddingValues.Zero,
                orderUiState = OrderUiState()
            )
        }
    }
@Composable
fun Skeleton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .pulseSkeleton(
                    lightColor = Color.LightGray.copy(alpha = 0.2f),
                    darkColor = Color.Gray.copy(alpha = 0.5f),
                    duration = 1800
                )
        )
        Box {  }
        Spacer(Modifier.width(12.dp))

    }
}