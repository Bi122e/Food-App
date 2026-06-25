package com.example.foodapp.ui.screen.preview

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.core.utils.timeAgo
import com.example.foodapp.domain.model.RatingCount
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.RestaurantPreview
import com.example.foodapp.presentation.extentions.getChipStyle
import com.example.foodapp.ui.screen.preview.section.HeaderContentPreviewSelection
import com.example.foodapp.ui.screen.preview.section.HeaderPreviewSelection
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun PreviewRestaurantTab(
    onNavigationToBack: () -> Unit,
    restaurant: Restaurant,
    previews: List<RestaurantPreview>
) {

    Log.d("checkUi_previewRestaurantUiState", "state = ${restaurant}")


    var selectedTag by remember { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = {
            TopBarPreviewRestaurant(
                onNavigationToBack = onNavigationToBack
            )
        },
        containerColor = Color.White,

        ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {

            item {
                Spacer(Modifier.height(10.dp))
            }

            item {
                HeaderPreviewSelection(
                    restaurant = restaurant
                )
            }

            item {
                Spacer(Modifier.height(15.dp))
            }

            item {
                HeaderContentPreviewSelection(
                    onSelectedTag = { tag ->
                        selectedTag = tag
                    }
                )
            }

            item {
                ContentNotificationSection(
                    selectedTag = selectedTag,
                    previews = previews
                )
            }
        }

    }
}

@Composable
fun ContentNotificationSection(
    selectedTag: String?,
    previews: List<RestaurantPreview>,
) {

    when (selectedTag) {

        null -> {

            if (previews.isNotEmpty()) {
                Box(
                    modifier = Modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        previews.forEach { item ->

                            val firstName = item.userName.first().toString()  ?: ""
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.Top
                            ) {

                                //avatar
                                Box(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .size(35.dp)
                                        .background(
                                            firstName.getChipStyle().background,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = firstName.uppercase(),
                                        color = firstName.getChipStyle().font,
                                    )
                                }


                                Column(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    horizontalAlignment = Alignment.Start,

                                    ) {

                                    //name
                                    Text(
                                        modifier = Modifier.padding(start = 5.dp),
                                        text = item.userName,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Gray
                                    )

                                    //stars + date
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                                    ) {

                                        //stars
                                        Box() {

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                                modifier = Modifier.padding(start = 2.dp)
                                            ) {

                                                repeat(5) {

                                                    Icon(
                                                        Icons.Rounded.Star,
                                                        contentDescription = null,
                                                        tint = if (item.rating > it)
                                                            Yellow0
                                                        else
                                                            Gray100
                                                    )
                                                }
                                            }
                                        }

                                        //time
                                        Text(
                                            text = item.createdAt?.timeAgo() ?: "---",
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black.copy(0.2f),
                                            fontSize = 12.sp
                                        )
                                    }

                                    //MESSAGE
                                    if (item.message.isNotEmpty()) {
                                        Text(
                                            modifier = Modifier.padding(start = 7.dp, end = 16.dp),
                                            text = item.message,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.Black
                                        )

                                    }

                                    //message tags
                                    LazyRow(

                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 14.dp)
                                    ) {

                                        items(items = item.previewTags) { tag ->

                                            Box(
                                                modifier = Modifier
                                                    .padding(
                                                        start = 5.dp,
                                                        top = 5.dp,
                                                        bottom = 5.dp,
                                                        end = 2.dp
                                                    )
                                                    .coloredShadow(
                                                        colors = listOf(Color.Black),
                                                        alpha = 0.1f,
                                                        borderRadius = 10.dp,
                                                        blurRadius = 1.dp
                                                    )

                                                    .padding(horizontal = 6.dp, vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = tag,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }
                                    }

                                }
                            }
                        }

                    }
                }
            } else {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(R.drawable.bg_empty_page1),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 50.dp, top = 15.dp)
                            .size(130.dp)
                    )

                    Text(
                        text = "Chưa có đánh giá",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = Color.LightGray
                    )
                }
            }

        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarPreviewRestaurant(
    onNavigationToBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Đánh giá của bạn",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        modifier = Modifier.padding(horizontal = 16.dp),
        navigationIcon = {

            Box(
                modifier = Modifier
                    .background(
                        Color.Black.copy(0.1f),
                        CircleShape
                    )
                    .padding(6.dp)
                    .clickable(onClick = onNavigationToBack)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,

                    )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )

    )
}


@Preview
@Composable
fun RestaurantTabPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )

    PreviewRestaurantTab(
        onNavigationToBack = {},
        restaurant =
            Restaurant(
                ratingCount = RatingCount(
                    oneStars = 3,
                    twoStars = 5,
                    threeStars = 6,
                    fiveStars = 1
                )
            ),
        previews = listOf(
            RestaurantPreview(
                userName = "Hoa Hoa",
                message = "Định lượng ít hơn chi nhánh aasdasd",
                previewTags = listOf(
                    "Quá ít",
                    "ok"
                ),
                rating = 1
            ),
            RestaurantPreview(
                userName = "dat tran",
                previewTags = listOf(
                    "Quá ít",
                    "ok"
                ),
                rating = 1
            )
        )
    )

}

