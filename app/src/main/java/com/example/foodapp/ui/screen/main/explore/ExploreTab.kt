package com.example.foodapp.ui.screen.main.explore

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTimeFilled
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ClearAll
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.presentation.extensions.pulseSkeleton
 import com.example.foodapp.presentation.state.ExploreUiState
import com.example.foodapp.ui.screen.main.explore.section.SearchBar
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.BrightOrange
import com.example.foodapp.ui.theme.Brow0
import com.example.foodapp.ui.theme.BurntOrange
import com.example.foodapp.ui.theme.MediumGray
import kotlinx.coroutines.flow.distinctUntilChanged


@SuppressLint("ConfigurationScreenWidthHeight", "RememberReturnType")
@Composable
fun ExploreTab(
    restaurants: List<Restaurant>,
    onLoadMore: () -> Unit,
    onNavigationToRes: (String) -> Unit,
    tag: String?,
    onNavigationBack: () -> Unit,
    exploreUiState: ExploreUiState,
    onSearch: () -> Unit,
    onQueryChanged: (String) -> Unit,
) {

    val screenH = LocalConfiguration.current.screenHeightDp
    val screenW = LocalConfiguration.current.screenWidthDp
    val cardWidth = (screenH * 0.280).dp
    val listState = rememberLazyListState()
    val isReachedBottom by remember {
        derivedStateOf {
            val lastVisibilityItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibilityItem >= restaurants.lastIndex - 2 //làm vậy để khi đứng last current idx ko bị reload nhiều lần vì size lst đã tăng lên
        }
    }

    LaunchedEffect(listState) {


        snapshotFlow {
            val layoutInfo = listState.layoutInfo

            val totalItems = layoutInfo.totalItemsCount
            val lastVisible =
                layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            Pair(lastVisible, totalItems)
        }
            .distinctUntilChanged()
            .collect { (lastVisible, totalItems) ->

                Log.d(
                    "loadMoreRestaurants",
                    """
            lastVisible = $lastVisible
            totalItems = $totalItems
            """.trimIndent()
                )
                Log.d("Check_getAllRestaurants", "scroll dk1: $lastVisible")
                Log.d("Check_getAllRestaurants", "scroll dk2: $totalItems")
                Log.d("Check_getAllRestaurants", "scroll dk3: ${listState.firstVisibleItemIndex} > 0")
                Log.d("Check_getAllRestaurants", "scroll dk4: ${listState.firstVisibleItemScrollOffset} > 0")
                Log.d("Check_getAllRestaurants", "scroll dk5: ${restaurants.isNotEmpty()}")

                if (
                    lastVisible >= totalItems - 1 &&
                    (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0) &&
                    restaurants.isNotEmpty()
                ) {
                    Log.d("Check_getAllRestaurants", "scroll: run")
                    onLoadMore()
                }
            }

    }


    Scaffold(
        topBar = {
            TopExploreBar(
                onNavigationBack = onNavigationBack,
                tag = tag,
                exploreUiState = exploreUiState,
                onSearch = onSearch,
                onQueryChanged = onQueryChanged,

            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        if (restaurants.isNotEmpty()) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = paddingValues,

                ) {

                item {
                    Spacer(Modifier.height(20.dp))
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //lst tab
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {

                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 5.dp, bottom = 5.dp)
                                        .coloredShadow(
                                            colors = listOf(Color.Black.copy(0.1f)),
                                            alpha = 0.2f,
                                            blurRadius = 5.dp
                                        )
                                        .background(
                                            Color.White, CircleShape
                                        )
                                        .padding(vertical = 5.dp, horizontal = 15.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.ClearAll,
                                        contentDescription = null,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 5.dp, bottom = 5.dp)
                                        .coloredShadow(
                                            colors = listOf(Color.Black.copy(0.1f)),
                                            alpha = 0.2f,
                                            blurRadius = 5.dp
                                        )
                                        .background(
                                            Color.White, CircleShape
                                        )
                                        .padding(vertical = 5.dp, horizontal = 15.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        Text(
                                            text = "Sắp xếp",
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black.copy(0.7f)

                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = null,
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                itemsIndexed(
                    items = restaurants,
                ) { idx, restaurant ->
                    Spacer(Modifier.height(20.dp))


                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Max)
                            .clickable(
                                onClick = {
                                    onNavigationToRes(restaurant.restaurantId)
                                }
                            )
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model =
                                ImageRequest.Builder(LocalContext.current)
                                    .data(restaurant.coverImage)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size((screenW * 0.25f).dp)
                                .clip(RoundedCornerShape(20.dp)),
                            contentScale = ContentScale.Crop,
                            loading = {

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MediumGray, RoundedCornerShape(20.dp))
                                        .pulseSkeleton(
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),

//                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                        )
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(100.dp),
                                        painter = painterResource(R.drawable.icon_delivery1),
                                        contentDescription = null
                                    )
                                }
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MediumGray, RoundedCornerShape(20.dp))
                                        .pulseSkeleton(
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),

//                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                        ),
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
                    Spacer(Modifier.height(20.dp))

                    if (restaurants.lastIndex != idx) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Black.copy(0.1f))
                        )
                    }
                }
            }
        } else {


            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(5.dp ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(150.dp))

                Image(
                    painter = painterResource(
                        R.drawable.bg_emty_search
                    ),
                    contentDescription = null,
                    modifier = Modifier
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Không tìm thấy kết quả phù hợp",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Hãy đổi từ khóa hoặc tìm quán khác nhé!",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(0.5f)
                )
            }
        }



    }


}

@Composable
fun TopExploreBar(
    onNavigationBack: () -> Unit,
    tag: String?,
    exploreUiState: ExploreUiState,
    onSearch: () -> Unit,
    onQueryChanged: (String) -> Unit
) {

     Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 7.dp,
                ambientColor = Color.Transparent,
                spotColor = Color.Red.copy(0.5f)
            )
            .background(Color.White)


    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            onNavigationBack()
                        }
                    )
                    .coloredShadow(
                        colors = listOf(Color.Black.copy(0.1f)),
                        alpha = 0.2f,
                        blurRadius = 5.dp
                    )
                    .background(
                        Color.White, CircleShape
                    )
                    .padding(6.dp)
            )

            Spacer(Modifier.weight(1f))

            SearchBar(
                text = exploreUiState.text,
                tagHolder = tag,
                onNavigationToExplore = {
                    onSearch()
                }
            ) {
                onQueryChanged(it)
            }
        }
    }
}

@Preview
@Composable
fun PreviewExploreTab() {
    ExploreTab(
        restaurants = emptyList(),
//        restaurants = PreviewDataRestaurant.restaurants,
        onLoadMore = {},
        onNavigationToRes = {},
        tag = "cơm",
        onNavigationBack = {},
        exploreUiState = ExploreUiState(),
        onSearch = {},
        onQueryChanged = {}
    )
}