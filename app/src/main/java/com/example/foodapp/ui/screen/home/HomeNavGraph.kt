package com.example.foodapp.ui.screen.home

import CartTab
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.foodapp.core.utils.UiStateHandler
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.presentation.state.OrderEvent
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.presentation.viewmodel.CartViewModel
import com.example.foodapp.presentation.viewmodel.CheckoutViewModel
import com.example.foodapp.presentation.viewmodel.FoodAction
import com.example.foodapp.presentation.viewmodel.FoodDetailViewModel
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.OrderViewModel
import com.example.foodapp.presentation.viewmodel.PromotionViewModel
import com.example.foodapp.presentation.viewmodel.RestaurantViewModel
import com.example.foodapp.presentation.viewmodel.SharedViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.screen.home.tab.ChatTab
import com.example.foodapp.ui.screen.home.tab.CheckOutTab
import com.example.foodapp.ui.screen.home.tab.FoodDetailTab
import com.example.foodapp.ui.screen.home.tab.HomeTab
import com.example.foodapp.ui.screen.home.tab.OrderTab
import com.example.foodapp.ui.screen.home.tab.PaymentTab
import com.example.foodapp.ui.screen.home.tab.ProfileTabRoute
import com.example.foodapp.ui.screen.home.tab.RestaurantDetailTab
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("RestrictedApi")
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController,
    padding: PaddingValues
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
                val orderViewModel: OrderViewModel = hiltViewModel()
                val promotionState by promotionViewModel.uiState.collectAsStateWithLifecycle()
//                val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
                val searchQueryState by homeViewModel.searchQuery.collectAsStateWithLifecycle()
                val searchResultState by homeViewModel.searchResult.collectAsStateWithLifecycle()
                val categoriesState by homeViewModel.categories.collectAsStateWithLifecycle()
                val foodFeaturedState by homeViewModel.featureFoods.collectAsStateWithLifecycle()
                val restaurantState by homeViewModel.restaurants.collectAsStateWithLifecycle()
                val foodByRestaurant by homeViewModel.foodByRestaurant.collectAsStateWithLifecycle()
                val orderUiState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
//                val addressState = profileState.editProfile.address

                HomeTab(
                    address = "addressState",
                    paddingValues = padding,
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
                    },
                    orderUiState = orderUiState,
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


                val restaurantState by foodViewModel.restaurantState.collectAsStateWithLifecycle()
                val foodsState by foodViewModel.foodsState.collectAsStateWithLifecycle()
                val foodState by foodViewModel.foodState.collectAsStateWithLifecycle()
                val favoriteState by restaurantViewMode.favorites.collectAsStateWithLifecycle()
                val userState by authViewModel.authStatus.collectAsStateWithLifecycle()
//                val cartState by cartViewModel.cartState.collectAsStateWithLifecycle()
                val cartState by cartViewModel.uiCartState.collectAsStateWithLifecycle()
                val profileState by profileViewModel.uiState.collectAsStateWithLifecycle()
//                val addToCartState by cartViewModel.addToCartState.collectAsStateWithLifecycle()
//                val overallCartState by cartViewModel.cartState.collectAsStateWithLifecycle()
//                val foodState by homeViewModel.foodByRestaurant.collectAsStateWithLifecycle()

                Log.d("HomeNavGraph", "Restaurant state: $restaurantState")
                Log.d("HomeNavGraph", "food state------: $foodsState")

                // Lắng nghe các event từ FoodDetailViewModel (Add to cart trực tiếp hoặc Điều hướng), user click là effect tự động chạy
                LaunchedEffect(Unit) {
                    foodViewModel.event.collect { action ->
                        when (action) {
                            is FoodAction.AddToCart -> {
                                cartViewModel.addSimpleItem(action.food, action.restaurant)
                                Log.d("ADDCART", "LAUNCHED RUN ADD TO CART")
                                showToast(context, "add cart")
                            }
                            is FoodAction.OpenDetail -> {
                                showToast(context, "open nav")
                                showToast(context, "this opened from")
                                navController.navigate("food_detail_tab/${action.foodId}")
                            }
                            is FoodAction.ShowMessage -> {}
                        }
                    }
                }
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

            composable(route = "food_detail_tab/{foodId}") { backStackEntry ->
                val foodViewModel: FoodDetailViewModel = hiltViewModel()
                val cartViewModel: CartViewModel = hiltViewModel()
                val foodState by foodViewModel.foodState.collectAsStateWithLifecycle()
                val cartState by cartViewModel.uiCartState.collectAsStateWithLifecycle()
                val currentItem = cartState.currentEditingItem

                val foodId = backStackEntry.arguments?.getString("foodId") ?: ""
                LaunchedEffect(foodId) {
                    foodViewModel.loadDetailFood(foodId)

                }
                Log.d("FoodDetailTab", "food state: ${foodState}")
                Log.d("FoodDetailTab", "cart state: ${cartState}")

                UiStateHandler(foodState) { food ->
                    FoodDetailTab(
                        food = food,
                        cartState = cartState,
                        onSelectVariation = cartViewModel::selectVariation,
                        onStartEditing = cartViewModel::startEditing,
                        increaseQtyDetail = cartViewModel::increaseQtyDetail,
                        decreaseQtyDetail = cartViewModel::decreaseQtyDetail,
                        toAddCart = {
                            cartViewModel.addEditingItem()
                            navController.popBackStack()
                        }
                    )
                }
            }
            //payment
            composable(route = UserRoutes.PAYMENT) { backStackEntry ->
                val parentEntry = remember(backStackEntry) { //dung remember de tranh goi nhieu lan
                    navController.getBackStackEntry(UserRoutes.CHECKOUT_ROOT) //lay entry sceen trong back stack, bay gio paymen-checkout la con cua checkout root -> 2 man hinh dung chung 1 Viewmodel
                }

                val checkoutViewModel: CheckoutViewModel = hiltViewModel(parentEntry)
                val checkoutUiState by checkoutViewModel.checkoutUiState.collectAsStateWithLifecycle()
                PaymentTab(
                    onNavBack = {
                        navController.popBackStack()
                    },
                    onSelectPayment = { payment ->
                        checkoutViewModel.selectPayment(payment)
                    },
                    checkoutUiState = checkoutUiState
                )
            }

            //checkout
            navigation(
                startDestination = UserRoutes.CHECKOUT,
                route = UserRoutes.CHECKOUT_ROOT
            ){
                composable(route = UserRoutes.CHECKOUT) {backStackEntry ->
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(UserRoutes.CHECKOUT_ROOT)
                    }
//                    val parentEntry2 = remember(backStackEntry) {
//                        navController.getBackStackEntry(UserRoutes.ORDER)
//                    }
                    val checkoutViewModel: CheckoutViewModel = hiltViewModel(parentEntry)
                    val checkoutUiState by checkoutViewModel.checkoutUiState.collectAsStateWithLifecycle()
                    val orderViewModel: OrderViewModel = hiltViewModel()
                    CheckOutTab(
                        checkoutUiState = checkoutUiState,
                        increaseQty = { foodId, variations, quantity ->
                            checkoutViewModel.increaseQty(
                                foodId = foodId,
                                variations = variations,
                                quantity = quantity
                            ) },
                        decreaseQty = { foodId, variations, quantity ->
                            checkoutViewModel.decreaseQty(
                                foodId = foodId,
                                variations = variations,
                                quantity = quantity
                            ) },
                        onNavCash = {
                            navController.navigate(UserRoutes.PAYMENT)
                        },
                        onOrderClick = {paymentMethod ->
                            orderViewModel.placeOrder(paymentMethod = paymentMethod)
                        },
//                        onNavOrderTab = {
//                            navController.navigate(UserRoutes.ORDER)
//                        }
                    )

                    LaunchedEffect(orderViewModel) {
                        orderViewModel.event.collectLatest { event ->
                            when (event) {
                                is OrderEvent.NavigationToDetail -> {
                                    navController.navigate(UserRoutes.orderDetail(event.orderId))
                                }
                            }
                        }
                    }
                }
            }

            //order
            composable(route = "${UserRoutes.ORDER}/{orderId}") { backStackEntry ->
                val orderId = requireNotNull(backStackEntry.arguments?.getString("orderId") )

                val orderViewModel: OrderViewModel = hiltViewModel()
                val orderUiState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
                OrderTab(
                    orderUiState = orderUiState,
                    orderId = orderId
                )
            }
            navigation(
                startDestination = UserRoutes.CART,
                route = UserRoutes.CART_ROOT
            ) {
                 composable(UserRoutes.CART) {
                     val cartViewModel: CartViewModel = hiltViewModel()
                     val cartState by cartViewModel.uiCartState.collectAsStateWithLifecycle()
                     CartTab(
                         cartState = cartState,
                         onClickClearCart = cartViewModel::clearCart,
                         onClickGetBack = {
                             navController.popBackStack()
                         },
                         onClickNavCheckOut = {
                             navController.navigate(UserRoutes.CHECKOUT)
                         }
                     )
                }
            }




            navigation(
                startDestination = UserRoutes.PROFILE,
                route = UserRoutes.PROFILE_ROOT
            ) {
                composable(UserRoutes.PROFILE) {

                    ProfileTabRoute(
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
