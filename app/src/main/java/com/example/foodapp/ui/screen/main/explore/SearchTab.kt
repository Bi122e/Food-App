package com.example.foodapp.ui.screen.main.explore


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material.icons.rounded.SocialDistance
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.core.UiState

import com.example.foodapp.presentation.state.ExploreUiState
import com.example.foodapp.ui.fakeData.PreviewDataRestaurant
import com.example.foodapp.ui.screen.main.explore.section.SearchBar
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Yellow1


@Composable
fun SearchTab(
    exploreUiState: ExploreUiState,
    onNavigationToRestaurant: (String) -> Unit,
    onNavigationBack: () -> Unit,
    onNavigationExplore: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    onNavigationToExplore: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopSearchBar(
                onNavigationBack = onNavigationBack,
                exploreUiState = exploreUiState,
                onQueryChanged = onQueryChanged,
                onNavigationToExplore = onNavigationToExplore,
            )
        },
        containerColor = Color.White
    ) { paddingValues ->

        val list = listOf(
            "Gà rán", "Trà sữa", "Bún đậu", "Bún", "Cơm chiên", "Chè"
        )


        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Text(
                    text = "Đề xuất cho bạn",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(10.dp))
            }

            items(list.chunked(3)) { items ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    items.forEach { item ->

                        Box(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        onNavigationExplore(item)
                                    }
                                )
//                                .coloredShadow(
//                                    Gray85,
//                                    alpha = 1f,
//                                    blurRadius = 5.dp,
//                                    borderRadius = 30.dp
//                                )
                                .shadow(
                                    elevation = 2.dp,
                                    shape = RoundedCornerShape(30.dp),
                                    clip = false
                                )
                                .background(
                                    Color.White,
                                    RoundedCornerShape(
                                        30.dp
                                    )
                                )
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier
                                    .padding(start = 6.dp, end = 12.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.KeyboardDoubleArrowUp,
                                    contentDescription = null,
                                    tint = Yellow1
                                )

                                Text(
                                    text = item,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black.copy(0.7f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Có thể bạn sẽ thích",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(10.dp))
            }



            when (exploreUiState.restaurantSuggestion) {
                is UiState.Success -> {
                    val chunkItems = exploreUiState.restaurantSuggestion.data.chunked(3)

                    items(
                        items = chunkItems
                    ) { item ->

                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier

                        ) {

                            item.forEach { item ->

                                //container
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable(
                                            onClick = {
                                                onNavigationToRestaurant(item.restaurantId)
                                            }
                                        )
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {
                                        //img
                                        Box(
                                            modifier = Modifier
                                                .size(120.dp)
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(
                                                    Color.LightGray
                                                )
                                        ) {

                                            SubcomposeAsyncImage(
                                                model = item.coverImage,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                error = {
                                                    Image(
                                                        painter = painterResource(R.drawable.error),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(80.dp)
                                                    )
                                                },
                                                loading = {
//                                            LoadingBox(
//                                                modifier = Modifier.fillMaxSize()
//                                            )

                                                }
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .offset(x = (-10).dp, y = (6).dp)
                                                    .background(
                                                        Color.Black.copy(0.4f),
                                                        RoundedCornerShape(7.dp)
                                                    )
                                                    .padding(
                                                        top = 6.dp,
                                                        bottom = 12.dp,
                                                        start = 20.dp,
                                                        end = 12.dp
                                                    )
                                            ) {
                                                Text(
                                                    text = "⭐ ${item.rating}",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.Top

                                        ) {
//                                            Icon(
//                                                imageVector = Icons.Rounded.Verified,
//                                                contentDescription = null,
//                                                modifier = Modifier.size(18.dp),
//                                                tint = Blue0
//                                            )
//
//                                            Text(
//                                                text = "${item.restaurantName}",
//                                                modifier = Modifier.weight(1f),
//                                                fontSize = 17.sp,
//                                                minLines = 2,
//                                                maxLines = 2,
//                                                overflow = TextOverflow.Ellipsis,
//                                                fontWeight = FontWeight.SemiBold,
//                                                color = Color.Black.copy(0.7f)
//                                            )

                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        ) {
                                            val iconId = "verified_icon"

                                            val annotatedText = buildAnnotatedString {
                                                appendInlineContent(iconId, "[icon]")
                                                append(" ${item.restaurantName}")
                                            }

                                            val inlineContent = mapOf(
                                                iconId to InlineTextContent(
                                                    placeholder = Placeholder(
                                                        width = 18.sp,
                                                        height = 18.sp,
                                                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Verified,
                                                        contentDescription = null,
                                                        tint = Blue0
                                                    )
                                                }
                                            )

                                            Text(
                                                text = annotatedText,
                                                inlineContent = inlineContent,
                                                minLines = 2,
                                                modifier = Modifier.weight(1f),
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black.copy(0.7f)
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.SocialDistance,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = Color.Black.copy(0.5f)
                                            )

                                            Text(
                                                text = "${0.6} km",
                                                fontSize = 14.sp,
                                                minLines = 1,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black.copy(0.5f)
                                            )
                                        }


                                    }
                                }
                            }
                        }
                    }
                }

                is UiState.Loading -> {
                    Log.d("SearchTab_Suggestion", "Loading")
                }

                else -> {
                    Log.d("SearchTab_Suggestion", "Error - else")
                }
            }
        }
    }
}


@Composable
fun TopSearchBar(
    exploreUiState: ExploreUiState,
    onNavigationBack: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onNavigationToExplore: () -> Unit,
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
                        Color.White,
                        CircleShape
                    )
                    .padding(6.dp)
            )

            Spacer(Modifier.weight(1f))

            var isFocused by remember { mutableStateOf(false) }
            SearchBar(
                text = exploreUiState.text,
                tagHolder = null,
                onNavigationToExplore = onNavigationToExplore
            ) {
                onQueryChanged(it)
            }
            Log.d("search_tab", "${exploreUiState.text}")
        }
    }
}

@Preview
@Composable
fun PreviewQueryTab() {
    SearchTab(
        exploreUiState = ExploreUiState(
            restaurantSuggestion = UiState.Success(PreviewDataRestaurant.restaurants)
        ),
        onNavigationToRestaurant = {},
        onNavigationBack = {},
        onNavigationExplore = {},
        onQueryChanged = {},
        onNavigationToExplore = {},
    )
}