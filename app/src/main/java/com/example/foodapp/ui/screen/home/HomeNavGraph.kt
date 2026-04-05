//package com.example.foodapp.ui.screen.home
//
//import CartTab
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.navigation
//import com.example.foodapp.core.UserRoutes
//import com.example.foodapp.presentation.viewmodel.FoodDetailViewModel
//import com.example.foodapp.presentation.viewmodel.HomeViewModel
//import com.example.foodapp.presentation.viewmodel.ProfileViewModel
//import com.example.foodapp.presentation.viewmodel.PromotionViewModel
//import com.example.foodapp.ui.screen.home.tab.ChatTab
//import com.example.foodapp.ui.screen.home.tab.HomeTab
//import com.example.foodapp.ui.screen.home.tab.ProfileTab
//import com.example.foodapp.ui.screen.home.tab.RestaurantDetailTab
//
//
////@Composable
////fun HomeNavGraph(
////    navController: NavHostController,
////    modifier: Modifier = Modifier,
//////    promotionState: UiState<List<Promotion>>,
//////    profileState: ProfileUiState
////) {
////    NavHost(
////        navController = navController,
////        startDestination = UserRoutes.HOME,
////        modifier = modifier,
////    ) {
////        composable(UserRoutes.HOME) {
////
////            val promotionViewModel: PromotionViewModel = hiltViewModel()
////            val profileViewModel: ProfileViewModel = hiltViewModel()
////            val homeViewModel: HomeViewModel = hiltViewModel()
////
////
////            val promotionState by promotionViewModel.uiState.collectAsStateWithLifecycle()
////            val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
////            val searchQueryState by homeViewModel.searchQuery.collectAsStateWithLifecycle()
////            val searchResultState by homeViewModel.searchResult.collectAsStateWithLifecycle()
////            val addressState = profileState.editProfile.address
////            val categoriesState by homeViewModel.categories.collectAsStateWithLifecycle()
////            val foodFeaturedState by homeViewModel.featureFoods.collectAsStateWithLifecycle()
////
////            HomeTab(
////                promotionState = promotionState,
////                profileState = profileState,
////                searchQueryState = searchQueryState,
////                searchResultState = searchResultState,
////                address = addressState,
////                onQueryChange = homeViewModel::updateSearchQuery,
////                categoryState = categoriesState,
////                featuredFoodState = foodFeaturedState,
////                modifier = Modifier.fillMaxSize(),
////                )
////        }
////
////        composable(UserRoutes.CHAT) { ChatTab() }
////        composable(UserRoutes.CART) { CartTab() }
////        composable(UserRoutes.PROFILE) { ProfileTab() }
////    }
////}
//@Composable
//fun HomeNavGraph(
//    navController: NavHostController,
//    parentNavController: NavHostController
//
//) {
//    NavHost(
//        navController = navController,
////        startDestination = UserRoutes.HOME,
//        startDestination = "home_root",
//                modifier = Modifier.fillMaxSize()
//    ) {
//
//        navigation(
//            startDestination = UserRoutes.HOME,
//            route = "home_root"
//        ) {
//
//        }
//        composable(UserRoutes.HOME) {
//
//            val promotionViewModel: PromotionViewModel = hiltViewModel()
//            val profileViewModel: ProfileViewModel = hiltViewModel()
//            val homeViewModel: HomeViewModel = hiltViewModel()
//            val foodDetailViewModel: FoodDetailViewModel = hiltViewModel()
//
//            val promotionState by promotionViewModel.uiState.collectAsStateWithLifecycle()
//            val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
//            val searchQueryState by homeViewModel.searchQuery.collectAsStateWithLifecycle()
//            val searchResultState by homeViewModel.searchResult.collectAsStateWithLifecycle()
//            val categoriesState by homeViewModel.categories.collectAsStateWithLifecycle()
//            val foodFeaturedState by homeViewModel.featureFoods.collectAsStateWithLifecycle()
//            val restaurantState by homeViewModel.restaurants.collectAsStateWithLifecycle()
//
//            val addressState = profileState.editProfile.address
//
//            HomeTab(
//                address = addressState,
//                promotionState = promotionState,
//                profileState = profileState,
//                searchQueryState = searchQueryState,
//                searchResultState = searchResultState,
//                onQueryChange = homeViewModel::updateSearchQuery,
//                categoryState = categoriesState,
//                featuredFoodState = foodFeaturedState,
//                restaurantState = restaurantState,
//                onClick = {navController.navigate(UserRoutes.RESTAURANTDETAIL)}
//            )
//        }
//
//        composable(UserRoutes.CHAT) { ChatTab() }
//        composable(UserRoutes.CART) { CartTab() }
////        composable(UserRoutes.PROFILE) { ProfileTab() }
//        composable(UserRoutes.PROFILE) {
//            ProfileTab(
////                onLogout = {
////                    // Ví dụ dùng parentNavController để ra ngoài graph
////                    parentNavController.navigate(UserRoutes.LOGIN) {
////                        popUpTo(0)
////                    }
////                }
//            )
//        }
//        composable(UserRoutes.RESTAURANTDETAIL) {
//            RestaurantDetailTab()
//        }
//    }
//}

package com.example.foodapp.ui.screen.home

import CartTab
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.foodapp.core.UiState
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.presentation.viewmodel.CartViewModel
import com.example.foodapp.presentation.viewmodel.FoodAction
import com.example.foodapp.presentation.viewmodel.FoodDetailViewModel
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.PromotionViewModel
import com.example.foodapp.presentation.viewmodel.RestaurantViewModel
import com.example.foodapp.presentation.viewmodel.SharedViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.screen.home.tab.ChatTab
import com.example.foodapp.ui.screen.home.tab.HomeTab
import com.example.foodapp.ui.screen.home.tab.ProfileTab
import com.example.foodapp.ui.screen.home.tab.RestaurantDetailTab

@SuppressLint("RestrictedApi")
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController
) {

    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = UserRoutes.HOME_ROOT,
        modifier = Modifier.fillMaxSize()
    ) {

        navigation(
            startDestination = UserRoutes.HOME,
            route = UserRoutes.HOME_ROOT
        ) {

            composable(UserRoutes.HOME) {
                Log.d("NAV_DEBUG", "Home Recomposed")

                val promotionViewModel: PromotionViewModel = hiltViewModel()
//                val profileViewModel: ProfileViewModel = hiltViewModel()
                val homeViewModel: HomeViewModel = hiltViewModel()

                val promotionState by promotionViewModel.uiState.collectAsStateWithLifecycle()
//                val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
                val searchQueryState by homeViewModel.searchQuery.collectAsStateWithLifecycle()
                val searchResultState by homeViewModel.searchResult.collectAsStateWithLifecycle()
                val categoriesState by homeViewModel.categories.collectAsStateWithLifecycle()
                val foodFeaturedState by homeViewModel.featureFoods.collectAsStateWithLifecycle()
                val restaurantState by homeViewModel.restaurants.collectAsStateWithLifecycle()
                val foodByRestaurant by homeViewModel.foodByRestaurant.collectAsStateWithLifecycle()

//                val addressState = profileState.editProfile.address

                HomeTab(
                    address = "addressState",
                    promotionState = promotionState,
//                    profileState = profileState,
                    searchQueryState = searchQueryState,
                    searchResultState = searchResultState,
                    onQueryChange = homeViewModel::updateSearchQuery,
                    categoryState = categoriesState,
                    featuredFoodState = foodFeaturedState,
                    restaurantState = restaurantState,
                    onClick = { foodId, restaurant, restaurantId ->
//                        homeViewModel.loadFoodByRestaurant(restaurant.restaurantId)
                        navController.navigate(
                            "restaurant/${restaurant.restaurantId}/$foodId"
                        )
                        val route = "restaurant/${restaurant.restaurantId}/$foodId"
                        Log.d("HomeNavGraph", route)
                    }
                )
            }

            composable(
                route = "restaurant/{restaurantId}/{foodId}",
                arguments = listOf(
                    navArgument("restaurantId") { type = NavType.StringType },
                    navArgument("foodId") { type = NavType.StringType }
                )
            ) { backStackEntry ->

                val restaurantId = backStackEntry.arguments?.getString("restaurantId")
                //hệ thống không đảm bảo có dữ liệu nên null
                val foodId = backStackEntry.arguments?.getString("foodId")

                Log.d(
                    "HomeNavGraph",
                    "Navigated to detail with restaurantId=$restaurantId, foodId=$foodId"
                )

                val foodViewModel: FoodDetailViewModel = hiltViewModel()
                val homeViewModel: HomeViewModel = hiltViewModel()
                val authViewModel: AuthViewModel = hiltViewModel()
                val restaurantViewMode: RestaurantViewModel = hiltViewModel()
                val profileViewModel: UserProfileViewModel = hiltViewModel()
                val cartViewModel: CartViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    if (foodId != null && restaurantId != null) {
                        foodViewModel.loadFoodByRestaurant(restaurantId)
                        foodViewModel.loadRestaurantById(restaurantId)
                    }
                }
                // Load data based on foodId
                LaunchedEffect(foodId) {
                    if (foodId != null) {
                        Log.d("HomeNavGraph", "Loading detail food for foodId=$foodId")
//                        foodViewModel.loadDetailFood(foodId)
                    } else {
                        Log.e("HomeNavGraph", "foodId is null, cannot load data")
                    }
                }
                val context = LocalContext.current
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Log.d("NavigationLog", "Current route: $currentRoute")

                // Lắng nghe các event từ FoodDetailViewModel (Add to cart trực tiếp hoặc Điều hướng)
                LaunchedEffect(Unit) {
                    foodViewModel.event.collect { action ->
                        when (action) {
                            is FoodAction.AddToCart -> {
                                cartViewModel.addToCart(action.food, action.restaurant )
                                Log.d("ADDCART", "LAUNCHED RUN ADD TO CART")
                                showToast(context, "add cart")
                            }
                            is FoodAction.OpenDetail -> {
                                showToast(context, "open nav")
                            }
                            is FoodAction.ShowMessage -> {}
                        }
                    }
                }
                val restaurantState by foodViewModel.restaurantState.collectAsStateWithLifecycle()
                val foodsState by foodViewModel.foodsState.collectAsStateWithLifecycle()
                val favoriteState by restaurantViewMode.favorites.collectAsStateWithLifecycle()
                val userState by authViewModel.authStatus.collectAsStateWithLifecycle()
                val cartState by cartViewModel.cartState.collectAsStateWithLifecycle()
                val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
                val addToCartState by cartViewModel.addToCartState.collectAsStateWithLifecycle()
                val overallCartState by cartViewModel.cartState.collectAsStateWithLifecycle()
//                val foodState by homeViewModel.foodByRestaurant.collectAsStateWithLifecycle()

                Log.d("HomeNavGraph", "Restaurant state: $restaurantState")
                Log.d("HomeNavGraph", "food state------: $foodsState")
//                val foods = when (foodsState) {
//                    is UiState.Success -> {(foodsState as? UiState.Success)?.data ?: emptyList()
//                     }
//                    is UiState.Loading -> emptyList() // hoặc show loading
//                    else -> emptyList()
//                }
//                Log.d("HomeViewModel", "final state------- $foodState")
                val restaurant = (restaurantState as? UiState.Success)?.data
                val favorite = (favoriteState as? UiState.Success)?.data
                val foods = when (foodsState) {
                    is UiState.Success -> (foodsState as UiState.Success).data
                    is UiState.Loading -> emptyList()
                    else -> emptyList()
                }
//                RestaurantDetailTab(restaurant, foodId, foods = (foodsState as? UiState.Success)?.data ?: emptyList() )
                RestaurantDetailTab(
                    restaurantState = restaurantState,
                    foodId = foodId,
                    cartState = cartState,
                    overallCartState = overallCartState,
                    foodsState = foodsState,
                    favoriteState = favoriteState,
                    onClickFavorite = restaurantViewMode::toggleFavorite,
                    profileState = profileState,
                    onClickViewCart = {
                        navController.navigate(UserRoutes.CART)
                    },
                    onClickAddCart = { food ->
                        if (profileState.profileCompleteness == ProfileCompleteness.INCOMPLETE) {
                            // User chưa hoàn thiện profile -> Lưu lại route để quay lại sau khi xong
                            val currentRoute = "restaurant/${restaurantId}/${foodId}"
                            sharedViewModel.savePendingRoute(currentRoute)
//                            sharedViewModel.savePendingItem(food.variations)
                            navController.navigate(UserRoutes.PROFILE) {
                                popUpTo(currentRoute) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            // Profile đã OK -> Báo ViewModel xử lý việc chọn món (check variation)
//                            foodViewModel.selectedFood(food)
                            foodViewModel.selectedFood(food)
                        }
                    },
//                    selectedFood = {},
                    onClickBackHome = {
//                        navController.navigate(UserRoutes.HOME) {
//                            popUpTo(navController.graph.findStartDestination().id) {
//                                inclusive = false
//                                saveState = true
//                            }
                        navController.popBackStack()


                        Log.d("HomeNavGraph", route ?: " fail")
                    })
            }


            navigation(
                startDestination = UserRoutes.CHAT,
                route = UserRoutes.CHAT_ROOT
            ) {
                composable(UserRoutes.CHAT) {
                    ChatTab()
                }

            }



            navigation(
                startDestination = UserRoutes.CART,
                route = UserRoutes.CART_ROOT
            ) {
                composable(UserRoutes.CART) {
                    CartTab()
                }
            }

            navigation(
                startDestination = UserRoutes.PROFILE,
                route = UserRoutes.PROFILE_ROOT
            ) {
                composable(UserRoutes.PROFILE) {
                    ProfileTab(
                        onClickBack = { navController.popBackStack() },
                        onProfileCompleted = {
                            sharedViewModel.consumePendingRoute()?.let { route ->
                                navController.navigate(route) {
                                    popUpTo(UserRoutes.PROFILE) { inclusive = true }
                                }
                            }
                        }
                    )
                }


            }
        }
    }
}
