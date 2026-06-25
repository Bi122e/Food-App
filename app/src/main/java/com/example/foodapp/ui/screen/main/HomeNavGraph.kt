package com.example.foodapp.ui.screen.main

import CartTab
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.foodapp.core.Routes
import com.example.foodapp.core.UiState
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.inRouteSnackBar
import com.example.foodapp.core.utils.UiStateHandler
import com.example.foodapp.presentation.extentions.toConvertTag
import com.example.foodapp.presentation.state.CompleteEventState
import com.example.foodapp.presentation.state.HomeData
import com.example.foodapp.presentation.state.HomeUiState
import com.example.foodapp.presentation.state.OrderEvent
import com.example.foodapp.presentation.viewmodel.AppNotificationViewModel
import com.example.foodapp.presentation.viewmodel.CartViewModel
import com.example.foodapp.presentation.viewmodel.CheckoutViewModel
import com.example.foodapp.presentation.viewmodel.CompleteViewModel
import com.example.foodapp.presentation.viewmodel.ExploreViewModel
import com.example.foodapp.presentation.viewmodel.FoodAction
import com.example.foodapp.presentation.viewmodel.FoodDetailViewModel
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.OrderViewModel
import com.example.foodapp.presentation.viewmodel.PreviewRestaurantViewModel
import com.example.foodapp.presentation.viewmodel.PromotionViewModel
import com.example.foodapp.presentation.viewmodel.RestaurantViewModel
import com.example.foodapp.presentation.viewmodel.UserProfileViewModel
import com.example.foodapp.ui.screen.preview.PreviewRestaurantTab
import com.example.foodapp.ui.screen.main.chat.ChatTab
import com.example.foodapp.ui.screen.main.checkout.CheckOutTab
import com.example.foodapp.ui.screen.main.explore.ExploreTab
import com.example.foodapp.ui.screen.main.food.FoodDetailTab
import com.example.foodapp.ui.screen.main.home.HomeTab
import com.example.foodapp.ui.screen.main.home.section.LoadingHomeTab
import com.example.foodapp.ui.screen.main.order.OrderTab
import com.example.foodapp.ui.screen.main.checkout.PaymentTab
import com.example.foodapp.ui.screen.main.complete.CompleteTab
import com.example.foodapp.ui.screen.main.complete.section.CompleteNotificationSection
import com.example.foodapp.ui.screen.main.profile.ProfileTabRoute
import com.example.foodapp.ui.screen.main.restaurant.RestaurantDetailTab
import com.example.foodapp.ui.screen.main.explore.SearchTab
import com.example.foodapp.ui.screen.main.notification.NotificationTab
import com.example.foodapp.ui.screen.main.profile.InfoRoute
import com.example.foodapp.ui.screen.shared.LoadingScreen
import com.example.foodapp.ui.screen.shared.SnackBarSuccessOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

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

    val activity = LocalActivity.current as ComponentActivity
    val orderViewModel: OrderViewModel = hiltViewModel(activity)
    val orderState by orderViewModel.orderUiState.collectAsStateWithLifecycle()
    var showSnackBar by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val context = LocalContext.current
        val currentRoute = navController
            .currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route

        val inOrderRoute = currentRoute == ("${UserRoutes.ORDER}/{orderId}")

        Log.d(
            "Check_king_route",
            "$currentRoute and ${UserRoutes.ORDER}/{orderId}: checking Route= $inOrderRoute"
        )
        Log.d(
            "Check_king_route",
            "rating notification ${orderState.appNotificationOrder?.ratingNotificationSent}"
        )

//        Log.d(
//            "checking_inOrderRoute_flow",
//            "inROute = ${inOrderRoute}, ratingNoti = ${orderState.appNotificationOrder?.ratingNotificationSent ?: false} ---->" +
//                    "result = ${
//                        inOrderRoute &&
//                                orderState.appNotificationOrder?.ratingNotificationSent ?: false
//                    }"
//        )


        LaunchedEffect(
            inOrderRoute,
            orderState.appNotificationOrder?.ratingNotificationSent
        ) {
            Log.d("checking_inOrderRoute_flow", "run ->")
            Log.d("OrderFlow", "UI RECEIVED EVENT")
            if (
                inOrderRoute &&
                (orderState.appNotificationOrder?.ratingNotificationSent ?: false)
            ) {
                Log.d(
                    "check_in_route",
                    "run ->>>>>>>>>>>>>>>>>>>>"
                )
                delay(3000)
                navController.navigate(UserRoutes.completeDetail("testId", "testsau")) //FIX SAU
                orderViewModel.resetNotification()
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavHost(
                navController = navController,
                startDestination = UserRoutes.HOME,
                modifier = Modifier.fillMaxSize()
            ) {


                composable(
                    route = UserRoutes.COMPLETE,
                    arguments = listOf(

                        navArgument(
                            name = "orderId",
                            builder = {
                                type = NavType.StringType
                            }
                        ),
                        navArgument(
                            "notificationId"
                        ) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val notificationId = backStackEntry.arguments?.getString("notificationId") ?: return@composable
                    Log.d("check_id_notificationTab", "current route = ${navController.currentDestination?.route}")

                    val orderId =
                        backStackEntry.arguments?.getString("orderId") ?: return@composable

                    val activity = LocalActivity.current as ComponentActivity
                    val completeViewModel: CompleteViewModel = hiltViewModel(activity)
                    val completeUiState by completeViewModel.completeUiState.collectAsStateWithLifecycle()

                    var showSuccess by remember { mutableStateOf(false) }
                    Log.d("check_state_after_key", "$orderId")

                    LaunchedEffect(Unit) {

                        completeViewModel.loadOrder(orderId = orderId)

                        completeViewModel.event.collect { even ->
                            when (even) {
                                is CompleteEventState.Error -> {
                                    Log.d("checkUI_CompleteEventState", "ERROR ${even.message}")
                                }
                                is CompleteEventState.Success -> {
                                    showSuccess = true
                                }
                            }
                        }
                    }

                    Log.d("check_state_after_key", "$completeUiState")

                    CompleteTab(
                        onNavigationToBack = {
                            navController.popBackStack()
                        },
                        completeUiState = completeUiState,
                        onAddPreviewTag = {
                            completeViewModel.addPreviewTag(it)
                        },
                        onRemovePreviewTag = {
                            completeViewModel.removePreviewTag(it)
                        },
                        onChangedRating = {
                            completeViewModel.setRating(it)
                        },
                        onChangedPrivate = {
                            completeViewModel.setPrivate(it)
                        },
                        onChangedMessage = {
                            completeViewModel.setMessage(it)
                        },
                        onCreateComplete = {
                            completeViewModel.createComplete(orderId, notificationId)
                        }
                    )

                    if (showSuccess) {
                        CompleteNotificationSection(
                            uiState = completeUiState,
                            onNavigationToBack = {
                                showSuccess = false
                                navController.navigate(UserRoutes.HOME) {
                                    popUpTo(UserRoutes.HOME) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                         )
                    }
                }

                composable(UserRoutes.HOME) {
                    LaunchedEffect(Unit) {
                        Log.e("HOME_SCREEN", "COMPOSE RUN")
                    }


                    Log.d("NAV_DEBUG", "Home Recomposed")

                    val promotionViewModel: PromotionViewModel = hiltViewModel()
                    val orderViewModel: OrderViewModel = hiltViewModel(activity)
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
                            Log.d(
                                "onNavigationToMore",
                                "explore Tag = ${UserRoutes.exploreTag(it)}"
                            )
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
                                Log.d(
                                    "check_InitialRestaurants",
                                    "tag - value = $value, mod = $mod "
                                )
                                Log.d(
                                    "Check_getAllRestaurants",
                                    "tag - value = $value, mod = $mod "
                                )


                            } else {
                                exploreViewModel.searchRestaurants()
                                Log.d("searchRestaurants", "query")
                            }
                        }

                        Log.d(
                            "searchRestaurants",
                            "UserRoutes.EXPLORE ${exploreUiState.restaurants}"
                        )
                        when (val restaurants = exploreUiState.restaurants) {
                            is UiState.Success -> {
                                ExploreTab(
//                exploreUiState = exploreUiState
                                    restaurants = restaurants.data,
                                    onLoadMore = {
                                        exploreViewModel.loadMoreRestaurants()
                                    },
                                    onNavigationToRes = { restaurantId ->
                                        navController.navigate(
                                            UserRoutes.restaurantDetail(
                                                restaurantId
                                            )
                                        )
                                    },
                                    tag = value,
                                    onNavigationBack = {
                                        navController.popBackStack()
                                    },
                                    exploreUiState = exploreUiState,
                                    onSearch = {
                                        exploreViewModel.searchRestaurants()
                                        Log.d(
                                            "exploreTab",
                                            "onSearch - ${exploreUiState.restaurants}"
                                        )
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
                    route = UserRoutes.NOTIFICATION
                ) { it ->

                    val activity = LocalActivity.current as ComponentActivity
                    val notificationViewModel: AppNotificationViewModel = hiltViewModel(activity)
                    val notificationUiState by notificationViewModel.notificationUiState.collectAsStateWithLifecycle()
                    Log.d(
                        "check_route_notifi",
                        "state ${notificationUiState.notifications} is UiState.Success = ${notificationUiState.notifications is UiState.Success}"
                    )

                    Log.d("check_route_notifi", "loading")
                    if (notificationUiState.notifications is UiState.Success) {
                        NotificationTab(
                            notifications = (notificationUiState.notifications as UiState.Success).data,
                            onNavigationToCompleteTab = { orderId, notificationId ->
                                Log.d("check_id_notificationTab", "orderId: $orderId, notificationId: $notificationId")
                                navController.navigate(
                                    UserRoutes.completeDetail(orderId, notificationId )
                                )
                            },
                            onResetToRead = { notificationId ->
                                notificationViewModel.setMarkasRead(notificationId)
                            }
                        )
                    } else {
                        LoadingScreen()

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
                                }

                                is FoodAction.OpenDetail -> {
                                    navController.navigate("food_detail_tab/${action.foodId}")
                                }
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
                            navController.navigate(UserRoutes.CHECKOUT)
                        },
                        onClickAddCart = { food ->

                            foodViewModel.selectedFood(food)

                        },
                        onClickBackHome = {

                            navController.popBackStack()


                            Log.d("HomeNavGraph", route ?: " fail")
                        },
                        onDialogChange = cartViewModel::changeValueDialog,
                        onForceAddItem = cartViewModel::forceAddItem,
                        onNavigationToPreview = {
                            navController.navigate(UserRoutes.restaurantPreview(restaurantId))
                            Log.d("check_nav_restaurantPreview", "resId: $restaurantId, restaurantPreview: ${UserRoutes.restaurantPreview(restaurantId)}, review: ${UserRoutes.PREVIEW}")
                        }
                    )
                }

                composable(
                    route = UserRoutes.PREVIEW,
                    arguments = listOf(
                        navArgument(
                            "restaurantId",
                            builder = {
                                type = NavType.StringType
                            }
                        )
                    )
                ) { backStackEntry ->

                    val activity = LocalActivity.current as ComponentActivity
                    val previewRestaurantViewModel: PreviewRestaurantViewModel = hiltViewModel(activity)
                    val previewUiState by previewRestaurantViewModel.previewRestaurantUiState.collectAsStateWithLifecycle()

                    val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: return@composable

                    LaunchedEffect(Unit) {
                        previewRestaurantViewModel.observePreviews(restaurantId)
                        previewRestaurantViewModel.observeRestaurant(restaurantId)
                    }
                    if (previewUiState.restaurants is UiState.Success && previewUiState.previews is UiState.Success)
                    PreviewRestaurantTab(
                        onNavigationToBack = {
                        },
                        restaurant = (previewUiState.restaurants as UiState.Success).data,
                        previews = (previewUiState.previews as UiState.Success).data
                    )
                    else {
                        LoadingScreen()
                    }
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
                                Log.d("FoodDetailTab", "run addedting")

                                cartViewModel.addEditingItem()
//                        navController.popBackStack()
                            },
                            onDialogToClose = cartViewModel::changeValueDialog,
                            onForceAddItem = cartViewModel::forceAddItem
                        )
                    }

                    LaunchedEffect(Unit) {
                        Log.d("emmit___emit", "start UNit")
                        cartViewModel.eventGetBack.collect {
                            Log.d("emmit___emit", "VM unit")


                            navController.popBackStack()
                        }
                    }


                }
                //payment
                composable(route = UserRoutes.PAYMENT) { backStackEntry ->
                    val parentEntry =
                        remember(backStackEntry) { //dung remember de tranh goi nhieu lan
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
                    val cartViewModel: CartViewModel = hiltViewModel(parentEntry)

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
                                    navController.navigate(UserRoutes.orderDetail(event.orderId)) {
                                        popUpTo(UserRoutes.HOME) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                    cartViewModel.clearCart()
                                }
                            }
                        }
                    }
                }


                //order
                composable(route = "${UserRoutes.ORDER}/{orderId}") { backStackEntry ->
                    val orderId = requireNotNull(backStackEntry.arguments?.getString("orderId"))

                    val orderViewModel: OrderViewModel = hiltViewModel(activity)
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

                    ProfileTabRoute(
                        onClickBack = { navController.popBackStack() },
                        paddingValues = padding,
                        onNavigationToInfoTab = {
                            navController.navigate(UserRoutes.INFO)
                        }
                    )
                }

                composable(route = UserRoutes.INFO) {
                    InfoRoute(
                        onNavigationToBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
            Log.d(
                "check_dk_dungsai", "route = ${inRouteSnackBar(currentRoute ?: Routes.SPLASH)}"
            )
            Log.d(
                "check_dk_dungsai", "rating = ${
                    (orderState.appNotificationOrder?.ratingNotificationSent ?: false)
                }"
            )

            Log.d("checking_nhah_else", "1: ${inRouteSnackBar(currentRoute ?: Routes.SPLASH)}")
            Log.d(
                "checking_nhah_else",
                "2: ${orderState.appNotificationOrder?.ratingNotificationSent}"
            )

            if (inRouteSnackBar(currentRoute ?: Routes.SPLASH) &&
                (orderState.appNotificationOrder?.ratingNotificationSent ?: false)
            ) {
                showSnackBar = true

                orderViewModel.resetNotification()
            }

            Box(modifier = Modifier.padding(bottom = 30.dp)) {
                SnackBarSuccessOrder(
                    showSnackBar = showSnackBar,
                    onValueChange = {
                        showSnackBar = false
                    }
                )
            }
        }

    }

}


