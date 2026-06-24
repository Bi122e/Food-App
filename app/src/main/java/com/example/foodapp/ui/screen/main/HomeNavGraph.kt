package com.example.foodapp.ui.screen.home

import CartTab
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.foodapp.core.UiState
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.utils.UiStateHandler
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.ProfileCompleteness
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.User
import com.example.foodapp.presentation.extentions.toConvertTag
import com.example.foodapp.presentation.state.HomeData
import com.example.foodapp.presentation.state.HomeUiState
import com.example.foodapp.presentation.state.OrderEvent
import com.example.foodapp.presentation.viewmodel.AuthViewModel
import com.example.foodapp.presentation.viewmodel.CartViewModel
import com.example.foodapp.presentation.viewmodel.CheckoutViewModel
import com.example.foodapp.presentation.viewmodel.ExploreViewModel
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
import com.example.foodapp.ui.screen.home.tab.ExploreTab
import com.example.foodapp.ui.screen.home.tab.FoodDetailTab
import com.example.foodapp.ui.screen.home.tab.HomeTab
import com.example.foodapp.ui.screen.home.tab.LoadingHomeTab
import com.example.foodapp.ui.screen.home.tab.OrderTab
import com.example.foodapp.ui.screen.home.tab.PaymentTab
import com.example.foodapp.ui.screen.home.tab.ProfileTabRoute
import com.example.foodapp.ui.screen.home.tab.RestaurantDetailTab
import com.example.foodapp.ui.screen.home.tab.SearchTab
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.compose

@SuppressLint("RestrictedApi")
@Composable
fun HomeNavGraph(
    listState: LazyListState,
    homeViewModel: HomeViewModel,
    homeUiState: HomeUiState,
    navController: NavHostController,
    padding: PaddingValues,
    homeData: HomeData
) {

    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = UserRoutes.HOME,
        modifier = Modifier.fillMaxSize()
    ) {


        composable(UserRoutes.HOME) {
            LaunchedEffect(Unit) {
                Log.e("HOME_SCREEN", "COMPOSE RUN")
            }


            Log.d("NAV_DEBUG", "Home Recomposed")

            val promotionViewModel: PromotionViewModel = hiltViewModel()
            val orderViewModel: OrderViewModel = hiltViewModel()
            val promotionState by promotionViewModel.uiState.collectAsStateWithLifecycle()
            val searchQueryState by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            val searchResultState by homeViewModel.searchResult.collectAsStateWithLifecycle()
            val categoriesState by homeViewModel.categories.collectAsStateWithLifecycle()
            val foodFeaturedState by homeViewModel.featureFoods.collectAsStateWithLifecycle()
            val restaurantState by homeViewModel.restaurants.collectAsStateWithLifecycle()
            val orderUiState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
            LaunchedEffect(homeUiState) {
                Log.d("CheckStateHome", homeUiState.toString())
            }

            Log.e("VM_INSTANCE", homeViewModel.toString())

            val states = listOf(
                homeUiState.restaurants,
                homeUiState.restaurantByRandom,
                homeUiState.restaurantsByCategory,
            )



            HomeTab(
                listState = listState,
                homeData = homeData,
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
                homeUiState = homeUiState,
                onNavOrder = { orderId ->
                    navController.navigate(UserRoutes.orderDetail(orderId))
                    Log.d("Order_click", orderId)
                },
                address = "",
                onClickResRandom = { restaurantId ->
                    navController.navigate(UserRoutes.restaurantDetail(restaurantId))
                },
                onNavRiceRes = { restaurantId ->
                    navController.navigate(UserRoutes.restaurantDetail(restaurantId))
                },
                onNavAllRes = { restaurantId ->
                    navController.navigate(UserRoutes.restaurantDetail(restaurantId))
                },
                onNavigationToMore = {
                    navController.navigate(UserRoutes.exploreTag(it))
                    Log.d("check_InitialRestaurants", "tag home -  $it ")

                    Log.d("onNavigationToMore", "value = $it")
                    Log.d("onNavigationToMore", "explore Tag = ${UserRoutes.exploreTag(it)}")
                    Log.d("onNavigationToMore", "explore screen = ${UserRoutes.EXPLORE}")
                }
            )
        }

        navigation(
            startDestination = UserRoutes.SEARCH,
            route = UserRoutes.EXPLORE_ROOT
        ) {


            composable(
                route = UserRoutes.SEARCH,
            ) { backStackEntry ->

                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(UserRoutes.EXPLORE_ROOT)
                }
                val exploreViewModel: ExploreViewModel = hiltViewModel(parentEntry)
                val exploreUiState by exploreViewModel.exploreUiState.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    exploreViewModel.loadSuggestionRestaurant()
                }

                SearchTab(
                    exploreUiState = exploreUiState,
                    onNavigationToRestaurant = { restaurantId ->
                        navController.navigate(UserRoutes.restaurantDetail(restaurantId))
                    },
                    onNavigationBack = {
                        navController.popBackStack()
                    },
                    onNavigationExplore = { tag ->
                        navController.navigate(UserRoutes.exploreTag(tag))
                        Log.d("searchRestaurants", "tag home -  $tag ")
                    },
                    onQueryChanged = {
                        exploreViewModel.setQueryOrTag(it)

                    },
                    onNavigationToExplore = {
                        navController.navigate(UserRoutes.exploreQuery(exploreUiState.text))
                    }
                )
            }




            composable(
                route = UserRoutes.EXPLORE,
                arguments = listOf(
                    navArgument("mode") {
                        type = NavType.StringType
                        nullable = true
                    },
                    navArgument(
                        "value"
                    ) {
                        type = NavType.StringType
                        nullable = true

                    }
                )
            ) { backStackEntry ->

                val parenEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(UserRoutes.EXPLORE_ROOT)
                }

                val exploreViewModel: ExploreViewModel = hiltViewModel(parenEntry)
                val exploreUiState by exploreViewModel.exploreUiState.collectAsStateWithLifecycle()

                val mod = backStackEntry.arguments?.getString("mod")
                val value = backStackEntry.arguments?.getString("value")

                LaunchedEffect(mod, value) {
                    exploreViewModel.resetRestaurants()
                    if (mod == "tag") {
                        exploreViewModel.loadInitialRestaurants(value.toConvertTag())
                        exploreViewModel.setQueryOrTag(value ?: "tat-ca")
                        Log.d("check_InitialRestaurants", "tag - value = $value, mod = $mod ")
                        Log.d("Check_getAllRestaurants", "tag - value = $value, mod = $mod ")


                    } else {
                        exploreViewModel.searchRestaurants()
                        Log.d("searchRestaurants", "query")
                    }
                }

                Log.d("searchRestaurants", "UserRoutes.EXPLORE ${exploreUiState.restaurants}")
                when (val restaurants = exploreUiState.restaurants) {
                    is UiState.Success -> {
                        ExploreTab(
//                exploreUiState = exploreUiState
                            restaurants = restaurants.data,
                            onLoadMore = {
                                exploreViewModel.loadMoreRestaurants()
                            },
                            onNavigationToRes = { restaurantId ->
                                navController.navigate(UserRoutes.restaurantDetail(restaurantId))
                            },
                            tag = value,
                            onNavigationBack = {
                                navController.popBackStack()
                            },
                            exploreUiState = exploreUiState,
                            onSearch = {
                                exploreViewModel.searchRestaurants()
                                Log.d("exploreTab", "onSearch - ${exploreUiState.restaurants}")
                            },
                            onQueryChanged = {
                                exploreViewModel.setQueryOrTag(it)
                            }
                        )
                    }
                    is UiState.Loading -> {
                        LoadingHomeTab()
                    }
                    else -> {}
                }
            }
        }




        composable(
            route = UserRoutes.RESTAURANT,
            arguments = listOf(
                navArgument("restaurantId") { type = NavType.StringType },
//                navArgument("foodId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val restaurantId =
                backStackEntry.arguments?.getString("restaurantId") ?: return@composable
            //hệ thống ko đảm bảo có dữ liệu nên null
//            val foodId = backStackEntry.arguments?.getString("foodId")

            Log.d(
                "HomeNavGraph",
                "Navigated to detail with restaurantId=$restaurantId"
            )

            val foodViewModel: FoodDetailViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            val restaurantViewMode: RestaurantViewModel = hiltViewModel()
            val profileViewModel: UserProfileViewModel = hiltViewModel()
            val cartViewModel: CartViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                if (restaurantId != null) {
                    foodViewModel.loadFoodByRestaurant(restaurantId)
                    foodViewModel.loadRestaurantById(restaurantId)
                }
            }
            // Load data based on foodId
//            LaunchedEffect(foodId) {
//                if (foodId != null) {
//                    Log.d("HomeNavGraph", "Loading detail food for foodId=$foodId")
////                        foodViewModel.loadDetailFood(foodId)
//                } else {
//                    Log.e("HomeNavGraph", "foodId is null, cannot load data")
//                }
//            }
            val context = LocalContext.current
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            Log.d("NavigationLog", "Current route: $currentRoute")


            val restaurantState by foodViewModel.restaurantState.collectAsStateWithLifecycle()
            val foodsState by foodViewModel.foodsState.collectAsStateWithLifecycle()
            val favoriteState by restaurantViewMode.favorites.collectAsStateWithLifecycle()
            val cartState by cartViewModel.uiCartState.collectAsStateWithLifecycle()
            val profileState by profileViewModel.uiStateProfile.collectAsStateWithLifecycle()

            Log.d("HomeNavGraph", "Restaurant state: $restaurantState")
            Log.d("HomeNavGraph", "food state------: $foodsState")

            // Lắng nghe các event từ FoodDetailViewModel
            // (Add to cart trực tiếp hoặc Điều hướng), user click là effect tự động chạy
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


            RestaurantDetailTab(
                restaurantState = restaurantState,
                foodId = "foodId",
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
                        val currentRoute = UserRoutes.restaurantDetail(restaurantId)
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
                onClickBackHome = {

                    navController.popBackStack()


                    Log.d("HomeNavGraph", route ?: " fail")
                }
            )
        }



        composable(UserRoutes.CHAT) {
            ChatTab()
        }

        composable(route = "food_detail_tab/{foodId}") { backStackEntry ->
            val foodViewModel: FoodDetailViewModel = hiltViewModel()
            val cartViewModel: CartViewModel = hiltViewModel()
            val foodState by foodViewModel.foodState.collectAsStateWithLifecycle()
            val cartState by cartViewModel.uiCartState.collectAsStateWithLifecycle()

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
                    })
            }
        }
        //payment
        composable(route = UserRoutes.PAYMENT) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { //dung remember de tranh goi nhieu lan
                navController.getBackStackEntry(UserRoutes.CHECKOUT) //lay entry sceen trong back stack, bay gio paymen-checkout la con cua checkout root ->
                // 2 man hinh dung chung 1 Viewmodel
            }

            val checkoutViewModel: CheckoutViewModel = hiltViewModel(parentEntry)
            val checkoutUiState by checkoutViewModel.checkoutUiState.collectAsStateWithLifecycle()
            PaymentTab(
                onNavBack = {
                    navController.popBackStack()
                }, onSelectPayment = { payment ->
                    checkoutViewModel.selectPayment(payment)
                }, checkoutUiState = checkoutUiState
            )
        }

        //checkout
        composable(route = UserRoutes.CHECKOUT) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(UserRoutes.CHECKOUT)
            }
//
            val checkoutViewModel: CheckoutViewModel = hiltViewModel(parentEntry)
            val checkoutUiState by checkoutViewModel.checkoutUiState.collectAsStateWithLifecycle()
            val orderViewModel: OrderViewModel = hiltViewModel()
            CheckOutTab(
                checkoutUiState = checkoutUiState,
                increaseQty = { foodId, variations, quantity ->
                    checkoutViewModel.increaseQty(
                        foodId = foodId, variations = variations, quantity = quantity
                    )
                },
                decreaseQty = { foodId, variations, quantity ->
                    checkoutViewModel.decreaseQty(
                        foodId = foodId, variations = variations, quantity = quantity
                    )
                },
                onNavCash = {
                    navController.navigate(UserRoutes.PAYMENT)
                },
                onOrderClick = { paymentMethod ->
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


        //order
        composable(route = "${UserRoutes.ORDER}/{orderId}") { backStackEntry ->
            val orderId = requireNotNull(backStackEntry.arguments?.getString("orderId"))

            val orderViewModel: OrderViewModel = hiltViewModel()
            val orderUiState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
            OrderTab(
                orderUiState = orderUiState, orderId = orderId
            )
        }

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
                })
        }


        //profile
        composable(UserRoutes.PROFILE) {

            ProfileTabRoute(onClickBack = { navController.popBackStack() }, onProfileCompleted = {
                sharedViewModel.consumePendingRoute()?.let { route ->
                    navController.navigate(route) {
                        popUpTo(UserRoutes.PROFILE) { inclusive = true }
                    }
                }
            }, paddingValues = padding, onUpdateProfile = {

            }
            )
        }
    }
}


