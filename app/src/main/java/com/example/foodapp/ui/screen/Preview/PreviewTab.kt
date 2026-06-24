package com.example.foodapp.ui.screen.Preview

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Yellow0
import com.example.foodapp.ui.theme.Yellow1
import com.example.foodapp.ui.theme.Yellow2

@Composable
fun PreviewRestaurantTab(
    onNavigationToBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopBarPreviewRestaurant(
                onNavigationToBack = onNavigationToBack
            )
        },
        containerColor = Color.White,
        modifier = Modifier.padding(horizontal = 16.dp)

    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Spacer(Modifier.height(10.dp))
            }

            item {
                HeaderPreviewSelection()
            }

            item {
                Spacer(Modifier.height(15.dp))
            }

            item {
                ContentPreviewSelection()
            }
        }

    }
}

@Composable
fun ContentPreviewSelection() {


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {

        Text(
            text = "Đánh giá và bình luận",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .coloredShadow(
                        colors = listOf(
                            Color.Black
                        ),
                        alpha = 0.5f,
                        borderRadius = 30.dp,
                        blurRadius = 0.4.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Có ảnh/Bình luận",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Rounded.Star,
                    tint = Color.Transparent,
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .coloredShadow(
                        colors = listOf(
                            Color.Black
                        ),
                        alpha = 0.5f,
                        borderRadius = 30.dp,
                        blurRadius = 0.4.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {

                    Text(
                        text = "Số sao",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        tint = Yellow0,
                        contentDescription = null
                    )

                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        modifier = Modifier.size(24.dp),
                        contentDescription = null
                    )
                }

            }
        }
    }
}
@Composable
fun HeaderPreviewSelection() {

    val items = listOf(5,4,3,2,1)
    val itemsData = listOf(171, 1, 4 ,2,3)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "4.9",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items.forEach { items ->

                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = Yellow0
                        )
                    }
                }


                Text(
                    text = "181 đánh giá",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black.copy(0.5f)
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),

        ) {

            items.forEach { item ->

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Text(
                        text = "$item (${itemsData[item-1]})",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(0.4f),
                    )

                    Box() {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .background(
                                    Gray100,
                                    RoundedCornerShape(30.dp)
                                )
                        )

                        if (item == 5) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp)
                                    .background(
                                        Yellow0,
                                        RoundedCornerShape(30.dp)
                                    )
                            )
                        }
                    }

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
        navigationIcon = {

            Box(
                modifier = Modifier
                    .background(
                        Color.Black.copy(0.4f),
                        CircleShape
                    )
                    .padding(6.dp)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopPreviewRestaurantSelection() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Đánh giá của quán",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null
            )
        }
    )
}

@Preview
@Composable
fun RestaurantTabPreview() {
    Box(Modifier.fillMaxSize().background(Color.White))

    PreviewRestaurantTab(
        onNavigationToBack = {}
    )
}

