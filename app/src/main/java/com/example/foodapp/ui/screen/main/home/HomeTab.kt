package com.example.foodapp.ui.screen.main.tab


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTimeFilled
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Discount
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.radialGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.Category
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Promotion
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.state.HomeData
import com.example.foodapp.presentation.state.HomeUiState
import com.example.foodapp.presentation.state.OrderUiState
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.preview.PreviewData
import com.example.foodapp.ui.preview.PreviewDataOrderState
import com.example.foodapp.ui.preview.PreviewDataRestaurant
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Blue4
import com.example.foodapp.ui.theme.BrightOrange
import com.example.foodapp.ui.theme.Brow0
import com.example.foodapp.ui.theme.BurntOrange
import com.example.foodapp.ui.theme.Gray
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.MediumGray
import com.example.foodapp.ui.theme.Yellow1
import com.google.android.gms.common.util.CollectionUtils.listOf

@Composable
fun HomeTab(
    listState: LazyListState,
    homeData: HomeData,
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
    onNavAllRes: (String) -> Unit,
    restaurantState: UiState<List<Restaurant>>,
    onClick: (foodId: String, restaurant: Restaurant, restaurantId: String) -> Unit,
    orderUiState: OrderUiState,
    homeUiState: HomeUiState,
    onNavigationToMore: (tag: String?) -> Unit,
    onNavOrder: (orderId: String) -> Unit,
    onClickResRandom: (String) -> Unit,
    onNavRiceRes: (String) -> Unit,
) {


    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var showProgress by remember { mutableStateOf(true) }
    var showExtendedProgress by remember { mutableStateOf(false) }
    val order = homeUiState.oder
    val screenH = LocalConfiguration.current.screenHeightDp
    val screenW = LocalConfiguration.current.screenWidthDp

    showToast(context, " home sc")
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .padding(paddingValues)
            .background(
//                brush = Brush.verticalGradient(
//                    0.0f to Color(0xff32c4e1),
//                    0.10f to PrimaryBlue,
//
//                    0.3f to Color(0xffebecee),
//                    0.7f to Color.White,
//                )
                color = Color.White
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
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
        ) {

//            item {
//                HeaderSection(address)
//                Spacer(Modifier.height(20.dp))
//            }

//            item {
//                SearchSection(
//                    query = searchQueryState,
//                    onValueChange = onQueryChange
//                )
//                Spacer(Modifier.height(20.dp))
//            }

//            item {
//                Spacer(Modifier.height((screenH * 0.035).dp))
//                PromotionSection(promotionState)
//                Spacer(Modifier.height(20.dp))
//            }
//
//            item {
//                CategorySelection(categoryState)
//                Spacer(Modifier.height(20.dp))
//            }

//            item {
//
//                FeaturedFoodSelection(
//                    featuredFoodState,
//                    restaurantState,
//                    onClick = onClick
//                )
//                Spacer(Modifier.height(20.dp))
//            }
            item {
                Box(
                    Modifier.background(Color.White)
                ) {
                    RandomResSelection(
                        homeData = homeData,
                        screenH = screenH,
                        screenW = screenW,
                        onLoadResRandomMore = {},
                        onClickResRandom = onClickResRandom,
                    )
                }

            }

            item {
                Spacer(Modifier.height(15.dp))
                RiceResSelection(
                    screenH = screenH,
                    screenW = screenW,
                    homeData = homeData,
                    onNavRiceResExtend = {},
                    onNavRiceRes = onNavRiceRes,
                    onNavigationToMore = onNavigationToMore,
                )
            }

//            item {
//                Spacer(Modifier.height(15.dp))
//
//                AllResSelection(
//                    screenH = screenH,
//                    screenW = screenW,
//                    homeData = homeData,
//                    onNavAllRes = onNavAllRes,
//                    onLoadResMore = onLoadResMore,
//
//                    )
//            }

//            item {
//                AllResSelectionHeader()
//            }

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    // gradient
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFFFFD66B),
                                        Color(0xFF8FE7D8)
                                    )
                                )
                            )
                    )

                    // fade
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colorStops = arrayOf(
                                        0.0f to Color.White,
                                        0.7f to Color.Transparent,
                                        0.7f to Color.Transparent,
                                        1f to Color.White
                                    )
                                )
                            )
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                    }
                                )
                        ) {
                            Text(
                                text = "📍 Món ngon mỗi ngày",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "Xem thêm",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                            onNavigationToMore(null)
                                        }
                                    )
                                    .padding(
                                        end = 16.dp
                                    )
                                    .background(
                                        Yellow1,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }


                        homeData.restaurants.forEach { restaurant ->

                            RestaurantCard(
                                restaurant = restaurant,
                                screenW = screenW,
                                screenH = screenH,
                                onNavAllRes = onNavAllRes
                            )
                        }
                    }
                }
            }

        }


        //circle order
        //order.any { !it.isFinished() } &&
        Log.d("CHECK_ORDER_state", "home ui state: ${homeUiState.oder.toString()}")
        Log.d("CHECK_ORDER_state", "order ui state: ${orderUiState.order.toString()}")
        if (showProgress && order.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-100).dp)
                    .size(60.dp)
                    .clickable(
                        onClick = {
                            if (order.size > 1) {
                                showProgress = false
                                showExtendedProgress = true
                            } else {
                                onNavOrder(order[0].orderId)
                                showToast(context, "clicked 1 item")
                            }
                        }
                    )
                    .background(
                        brush = radialGradient(
                            //colors = listOf(Gray65.copy(alpha = 0.7f), Blue1)
                            colors = listOf(
                                Color.White, Blue1.copy(alpha = 0.3f),
                            ),

                            ),
                        shape = CircleShape
                    )
                    .padding(6.dp),
                contentAlignment = Alignment.Center

            ) {
//                Icon(
//                    imageVector = Icons.Rounded.Circle,
//                    contentDescription = null,
//                    tint = Color.Red.copy(
//                        alpha = 0.5f
//                    ),
//                    modifier = Modifier
//                        .offset(x = (18).dp, y = (-12).dp)
//                        .size(12.dp)
//
//                )

                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(50.dp)
//                        .clickable {
//                            if (order.size > 1) {
//                                showProgress = false
//                                showExtendedProgress = true
//                            }
//                        }
                )

            }
        }

        if (showExtendedProgress) {
            val config = LocalConfiguration.current
            val screenH = config.screenHeightDp
            val screenW = config.screenWidthDp

//            val count = 5
            val isScrollable = order.size >= 4

            val offsetY = when {
                order.size >= 3 -> (screenH * 0.18f).dp
                else -> (screenH * 0.30f - order.size * (screenH * 0.012f)).dp
            }

            val listHeight = (screenH * 0.44f).dp
            val imageSize = (screenW * 0.14f).dp
            val iconSize = (screenW * 0.09f).dp
            val horizontalPad = (screenW * 0.04f).dp
            val cornerRadius = (screenW * 0.075f).dp
            val itemSpacing = (screenW * 0.025f).dp

            Box(
                modifier = Modifier
                    .padding(bottom = (screenH * 0.060).dp)
                    .align(Alignment.CenterEnd)
                    .fillMaxWidth()
                    .offset(y = offsetY)
                    .padding(horizontal = horizontalPad),
            ) {
                Box(
                    modifier = Modifier
                        .then(
                            if (isScrollable) Modifier.height(listHeight)
                            else Modifier.wrapContentHeight()
                        )
                        .background(Gray.copy(alpha = 0.8f), RoundedCornerShape(cornerRadius))
                        .border(4.dp, Gray100, RoundedCornerShape(cornerRadius))
                        .padding(horizontal = horizontalPad, vertical = 10.dp)
                ) {
                    LazyColumn {
                        items(order) { item ->
                            Box(
                                modifier = Modifier
                                    .padding(vertical = (screenH * 0.005f).dp)
                                    //5.dp * (screenH / 833f)
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = {
                                            onNavOrder(item.orderId)
                                            showToast(
                                                context,
                                                "clicked item ${item.orderId}"
                                            )
                                            Log.d("Order_click", item.orderId)
                                        }
                                    )
                                    .border(
                                        0.1.dp,
                                        Blue0.copy(0.5f),
                                        RoundedCornerShape(cornerRadius)
                                    )
                                    .background(Color.White, RoundedCornerShape(cornerRadius))
                                    .padding(vertical = 10.dp, horizontal = 10.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.bg_chef2),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(imageSize)
                                            .background(Gray, CircleShape)
                                            .padding(imageSize * 0.15f)
                                    )

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(itemSpacing * 0.6f)
                                    ) {
                                        Text(
                                            text = item.status.vietnameseLabel,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Blue0
                                        )
                                        Text(
                                            text = "Nhà hàng - ${item.restaurantName}",
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black.copy(alpha = 0.3f)
                                        )
                                        Text(
                                            text = "Tài xế | ${item.driverName ?: "Đang tìm tài xế"}",
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Icon(
                    imageVector = Icons.Rounded.Close,
                    tint = Color.Black.copy(alpha = 0.7f),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(iconSize)
                        .offset(y = -(iconSize * 0.28f), x = (iconSize * 0.28f))
                        .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                        .background(Color.White.copy(0.5f), CircleShape)
                        .clickable(
                            onClick = {
                                showToast(context = context, "clicked!")
                                showProgress = true
                                showExtendedProgress = false
                            }
                        )
                        .padding(iconSize * 0.14f)
                        .zIndex(1f)
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

@Composable
fun RandomResSelection(
    screenH: Int,
    screenW: Int,
    homeData: HomeData,
    onLoadResRandomMore: () -> Unit,
    onClickResRandom: (String) -> Unit,
) {
    val listStateRandomRes = rememberLazyListState()
    val listStateCateRes = rememberLazyListState()
    val listStateRes = rememberLazyListState()
    val context = LocalContext.current
//    LaunchedEffect(listStateRandomRes) {
//
//        snapshotFlow {
//            listStateRandomRes.layoutInfo.visibleItemsInfo.lastOrNull()?.index
//        }.collect { lastIdx ->
//
//            if (
//                lastIdx != null &&
//                lastIdx >= homeData.restaurantByRandom.lastIndex - 2
//            ) {
//                onLoadResRandomMore()
//            }
//        }
//    }

    Column(
        modifier = Modifier
            .padding(
                start = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = "\uD83C\uDF89",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier,
            )

            Text(
                text = "Món ngon dành cho bạn",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier,
            )
        }


        LazyRow(
            state = listStateRandomRes,
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {

            items(
                items = homeData.restaurantByRandom
            ) { item ->

                val cardWidth = (screenW * 0.370).dp
                Column(
                    modifier = Modifier.clickable(
                        onClick = {
                            showToast(context = context, item.restaurantName)
                            onClickResRandom(item.restaurantId)
                        }
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        Modifier.background(Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .height((screenH * 0.160).dp)
                                .width(cardWidth)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MediumGray)
                        ) {
                            SubcomposeAsyncImage(
                                model = item.coverImage,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),

                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            modifier = Modifier.size(70.dp),
                                            painter = painterResource(R.drawable.icon_delivery1),
                                            contentDescription = null
                                        )

                                    }
                                },
                                error = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),

                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            modifier = Modifier.size(70.dp),
                                            painter = painterResource(R.drawable.icon_delivery1),
                                            contentDescription = null
                                        )
                                    }
                                }
                            )

                        }
                    }
                    Text(
                        modifier = Modifier.width(cardWidth),
                        text = "${item.restaurantName}",
                        fontSize = 17.sp,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(0.7f)
                    )
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .background(
                                    BrightOrange.copy(0.1f),
                                    RoundedCornerShape(5.dp),
                                )
                                .padding(
                                    start = 5.dp,
                                    top = 5.dp,
                                    bottom = 5.dp,
                                    end = 5.dp
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = BurntOrange,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "${item.estimatedDeliveryTime} phút",
                                    color = BurntOrange,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        Box(
                            Modifier
                                .background(
                                    Blue2.copy(0.2f),
                                    RoundedCornerShape(5.dp),
                                )
                                .padding(
                                    start = 5.dp,
                                    top = 5.dp,
                                    bottom = 5.dp,
                                    end = 15.dp
                                )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Discount,
                                    contentDescription = null,
                                    tint = Blue0,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "${item.deliveryFee}k",
                                    color = Blue0,
                                    fontSize = 9.sp
                                )
                            }

                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeTabPreview() {

     Scaffold(
//        topBar = {
//            HomeTopBar()
//        },
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
            onClick = { _, _, _ -> },
            paddingValues = PaddingValues.Absolute(),
            orderUiState = OrderUiState(
                order = listOf(
                    PreviewDataOrderState.previewOrder(),
                )
            ),
            homeUiState = HomeUiState(
                oder = listOf(
                    PreviewDataOrderState.previewOrder(),
                    PreviewDataOrderState.previewOrder()
                )
            ),
            onNavOrder = {},
            homeData = HomeData(
                oder = emptyList(),
                restaurants = PreviewDataRestaurant.restaurants,
                restaurantByRandom = PreviewDataRestaurant.restaurants,
                restaurantsByCategory = PreviewDataRestaurant.restaurants,
            ),
            onClickResRandom = {},
            onNavRiceRes = {},
            onNavAllRes = {},
            listState = LazyListState(),
            onNavigationToMore = {},
        )
    }
}

@Composable
fun RiceResSelection(
    screenH: Int,
    screenW: Int,
    homeData: HomeData,
    onNavRiceResExtend: () -> Unit,
    onNavRiceRes: (String) -> Unit,
    onNavigationToMore: (tab: String?) -> Unit,
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clickable(
                    onClick = {
                        showToast(context, "clicked")
                        onNavRiceResExtend()
                    }
                ),
        ) {
            Text(
                text = "🖐 Nay lại thèm cơm",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier,
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = null,

                modifier = Modifier
                    .clickable(
                        onClick = {
                            onNavigationToMore("com")
                        }
                    )
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Transparent
                    )
                    .background(Color.White, CircleShape)
                    .padding(4.dp)
            )

        }

        val cardWidth = (screenW * 0.370).dp
        val cardHeight = (screenH * 0.160).dp


        Box {
            Box(
                modifier = Modifier
                    .height((screenH * 0.180).dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Blue2.copy(0.0f),
                                Blue0.copy(0.7f),
                                Blue2.copy(0.0f)
                            )
                        )
                    ),
            ) {}

            //res rice container
            LazyRow(
                contentPadding = PaddingValues(end = 16.dp),
                modifier = Modifier
                    .padding(start = 16.dp, top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                items(
                    items = homeData.restaurantsByCategory
                ) { item ->

                    val cardWidth = (screenW * 0.370).dp

                    //sub res container
                    Column(
                        modifier = Modifier.clickable(
                            onClick = {
                                showToast(context = context, item.restaurantName)
                                onNavRiceRes(item.restaurantId)
                            }
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        //box img container
                        Box(
                            modifier = Modifier
                                .height((screenH * 0.160).dp)
                                .width(cardWidth)
                                .clip(RoundedCornerShape(20.dp))
                                .background(MediumGray)
                        ) {

                            SubcomposeAsyncImage(
                                model = item.coverImage,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),

                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            modifier = Modifier.size(70.dp),
                                            painter = painterResource(R.drawable.icon_delivery1),
                                            contentDescription = null
                                        )

                                    }
                                },
                                error = {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),

                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            modifier = Modifier.size(70.dp),
                                            painter = painterResource(R.drawable.icon_delivery1),
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                            //icon
                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .offset(x = (-5).dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .zIndex(1f)
                                        .background(
                                            Blue4,
                                            RoundedCornerShape(5.dp),
                                        )
                                        .padding(
                                            start = 15.dp,
                                            top = 5.dp,
                                            bottom = 5.dp,
                                            end = 5.dp
                                        )
                                ) {

                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = null,
                                        tint = Color.Yellow,
                                        modifier = Modifier.size(12.dp)
                                    )

                                    Text(
                                        text = "⭐ ${item.rating} (${item.totalReview})",
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 9.sp
                                    )

                                }

                                Box(
                                    Modifier
                                        .offset(x = (-7).dp)
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color(0xFF28a7aa),
                                                    Color(0xFF70dfe2)

                                                )
                                            ),
                                            RoundedCornerShape(5.dp),
                                        )
                                        .padding(
                                            start = 15.dp,
                                            top = 5.dp,
                                            bottom = 5.dp,
                                            end = 5.dp
                                        )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.ShoppingBag,
                                            contentDescription = null,
                                            tint = Brow0,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "10+",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    }
                                }

                            }
                        }

                        //res name
                        Row(
                            modifier = Modifier.width(cardWidth),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Verified,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Blue0
                            )

                            Text(
                                modifier = Modifier.width(cardWidth),
                                text = "${item.restaurantName}",
                                fontSize = 17.sp,
                                minLines = 2,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AllResSelectionHeader() {
    // Chỉ render phần background gradient + title
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // gradient cam -> xanh
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFFFD66B), Color(0xFF8FE7D8))
                    )
                )
        )
        // fade top/bottom
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.White,
                            0.3f to Color.Transparent,
                            0.7f to Color.Transparent,
                            1f to Color.White
                        )
                    )
                )
        )

        Text(
            text = "📍 Món ngon mỗi ngày",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 10.dp),
        )
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    screenW: Int,
    screenH: Int,
    onNavAllRes: (String) -> Unit,
) {
    val cardWidth = (screenH * 0.280).dp
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, Gray100, RoundedCornerShape(30.dp))
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(30.dp),
                spotColor = Color.Transparent
            )
            .background(Color.White, RoundedCornerShape(30.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable { onNavAllRes(restaurant.restaurantId) }
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SubcomposeAsyncImage(
                model = restaurant.coverImage,
                contentDescription = null,
                modifier = Modifier
                    .size((screenW * 0.25f).dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop,
                loading = {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MediumGray, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(70.dp),
                            painter = painterResource(R.drawable.icon_delivery1),
                            contentDescription = null
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MediumGray, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier.size(70.dp),
                            painter = painterResource(R.drawable.icon_delivery1),
                            contentDescription = null
                        )
                    }
                }
            )

            //info
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                //name
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Verified,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Blue0
                    )

                    Text(
                        modifier = Modifier.width(cardWidth),
                        text = "${restaurant.restaurantName}",
                        fontSize = 17.sp,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(0.7f)
                    )
                }

                Spacer(Modifier.weight(1f))


                //rating
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = BrightOrange,
                            modifier = Modifier.size(14.dp)
                        )


                        Text(
                            text = "${restaurant.rating} (${restaurant.totalReview})",
                            color = Color.Black.copy(0.5f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ShoppingBag,
                            contentDescription = null,
                            tint = Brow0,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "10+",
                            color = Color.Black.copy(0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccessTimeFilled,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${restaurant.estimatedDeliveryTime} phút",
                            color = Color.Black.copy(0.5f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }


                }


                //couple
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                ) {
                    Box(
                        Modifier
                            .background(
                                BrightOrange.copy(0.1f),
                                RoundedCornerShape(5.dp),
                            )
                            .padding(5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_ticket1),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "10%",
                                color = BurntOrange,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        Modifier
                            .background(
                                Blue2.copy(0.2f),
                                RoundedCornerShape(5.dp),
                            )
                            .padding(5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_box4),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "freeship 12.000đ",
                                color = Blue1,
                                fontSize = 12.sp
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun AllResSelection(
    screenH: Int,
    screenW: Int,
    homeData: HomeData,
    onNavAllRes: (String) -> Unit,
    onLoadResMore: () -> Unit,
) {


    val cardHeight = (screenH * 0.140).dp
    val cardWidth = (screenH * 0.280).dp
    val context = LocalContext.current


    //res container
    Box(

        Modifier
            .fillMaxWidth()
        //                .heightIn(min = screenH.dp)
    ) {


        // bên trái cam -> trắng
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
//                                Color(0xFFFFF7C2), // cam đậm hơn
//                                Color(0xFFCFF7F1), // xanh đậm hơn
                            Color(0xFFFFD66B),
                            Color(0xFF8FE7D8),
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.White,
                            0.45f to Color.Transparent,
                            0.55f to Color.Transparent,
                            1f to Color.White
                        )
                    )
                )
        )


        val lst = listOf(2)
        //restaurants
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(Modifier.height(10.dp))

            Text(
                text = "📍 Món ngon mỗi ngày",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.padding(start = 16.dp),
            )
            Spacer(Modifier.height(2.dp))

            homeData.restaurants.forEach { restaurant ->

                //box container
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Gray100,
                            RoundedCornerShape(30.dp)
                        )
                        .shadow(
                            elevation = 20.dp,
                            shape = RoundedCornerShape(30.dp),
                            ambientColor = Color.Black,
                            spotColor = Color.Transparent
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable(
                            onClick = {
                                onNavAllRes(restaurant.restaurantId)
                                showToast(context, restaurant.restaurantName)
                            }
                        )
                ) {

                    Row(
                        modifier = Modifier.height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = restaurant.coverImage,
                            contentDescription = null,
                            modifier = Modifier
                                .size((screenW * 0.25f).dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop,
                            loading = {

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MediumGray, RoundedCornerShape(20.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        modifier = Modifier.size(70.dp),
                                        painter = painterResource(R.drawable.icon_delivery1),
                                        contentDescription = null
                                    )
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MediumGray, RoundedCornerShape(20.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        modifier = Modifier.size(70.dp),
                                        painter = painterResource(R.drawable.icon_delivery1),
                                        contentDescription = null
                                    )
                                }
                            }
                        )

                        //info
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            //name
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Verified,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Blue0
                                )

                                Text(
                                    modifier = Modifier.width(cardWidth),
                                    text = "${restaurant.restaurantName}",
                                    fontSize = 17.sp,
                                    minLines = 2,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black.copy(0.7f)
                                )
                            }

                            Spacer(Modifier.weight(1f))


                            //rating
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Star,
                                        contentDescription = null,
                                        tint = BrightOrange,
                                        modifier = Modifier.size(14.dp)
                                    )


                                    Text(
                                        text = "${restaurant.rating} (${restaurant.totalReview})",
                                        color = Color.Black.copy(0.5f),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.ShoppingBag,
                                        contentDescription = null,
                                        tint = Brow0,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "10+",
                                        color = Color.Black.copy(0.5f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AccessTimeFilled,
                                        contentDescription = null,
                                        tint = Color.LightGray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "${restaurant.estimatedDeliveryTime} phút",
                                        color = Color.Black.copy(0.5f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }


                            }


                            //couple
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(7.dp),
                            ) {
                                Box(
                                    Modifier
                                        .background(
                                            BrightOrange.copy(0.1f),
                                            RoundedCornerShape(5.dp),
                                        )
                                        .padding(5.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_ticket1),
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "10%",
                                            color = BurntOrange,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Box(
                                    Modifier
                                        .background(
                                            Blue2.copy(0.2f),
                                            RoundedCornerShape(5.dp),
                                        )
                                        .padding(5.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(R.drawable.ic_box4),
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "freeship 12.000đ",
                                            color = Blue1,
                                            fontSize = 12.sp
                                        )
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }

        homeData
    }

}



