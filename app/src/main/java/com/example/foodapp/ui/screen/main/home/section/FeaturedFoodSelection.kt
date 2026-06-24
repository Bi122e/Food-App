package com.example.foodapp.ui.screen.main.home.section

import AppAsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Food
import com.example.foodapp.domain.model.Restaurant

@Composable
fun FeaturedFoodSelection(
    foodState: UiState<List<Food>>,
    restaurantState: UiState<List<Restaurant>>,
    onClick:  (foodId: String, restaurant: Restaurant, restaurantId: String) -> Unit,
) {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    ) {
        Text(
            text = "Món ăn nổi bật",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            modifier = Modifier
                .weight(1f),
        )

//        Box(
//            modifier = Modifier
//                .clip(RoundedCornerShape(20.dp))
//                .background(Color.Yellow)
//                .padding(6.dp),
//            contentAlignment = Alignment.CenterEnd,
//        ) {
//            Text(
//                text = "Xem tất cả",
//                color = Color.Black,
//                fontWeight = FontWeight.Bold,
//                fontSize = 12.sp,
//                textAlign = TextAlign.End,
//            )
//        }


    }

    Spacer(Modifier.height(10.dp))

    when {

        foodState is UiState.Success && restaurantState is UiState.Success -> {

            val foods = foodState.data
            val restaurantMap = restaurantState.data.associateBy { it.restaurantId }


            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
//                contentPadding =    (bottom = 32.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)


            ) {
                items(foods, key = { it.foodId }) { food ->

                    val restaurant = restaurantMap[food.restaurantId]
                    val restaurantName =
                        restaurant?.restaurantName ?: "Chưa xác định"

                    //shadow + main box
                    Box(
                        modifier = Modifier
                            .clickable{
                                restaurant?.let {
                                    onClick(
                                        food.foodId,
                                        restaurant,
                                        restaurant.restaurantId
                                    )
                                }

                            }
                            .shadow(
                                elevation = 4.dp,
                                shape = RoundedCornerShape(20.dp),
                                clip = false
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                        ) {

                            //column main
                            Column(
                                Modifier
                                .width(250.dp)
                            ) {

                                //sub box 1 img
                                Box(
                                    Modifier
                                        .height(100.dp)
                                ) {
                                    AppAsyncImage(
                                        imageUrl = food.imgUrl,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )

                                    //box time
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White)
                                            .padding(horizontal = 8.dp, vertical = 4.dp),

                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccessTime,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(14.dp)
                                            )
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                text = " ${food.foodTime} phút",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Normal
                                            )
                                        }

                                    }
                                }

                                //sub box 2
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {

                                    //column child
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        ) {

                                        //food name + restaurant name
                                        Text(
                                            text = "${food.name} - $restaurantName",
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp, vertical = 5.dp),
                                            maxLines = 2,
                                            fontSize = 14.sp,

                                            )


                                        //row = text + box
                                        Row (
                                            verticalAlignment = Alignment
                                                .CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {

                                            //review
                                            Text(
                                                text =
                                                    if (food.reviewCount > 0) {
                                                        "${food.averageRating} ⭐ (${food.reviewCount}+)"
                                                    } else "",
                                                fontWeight = FontWeight.Normal,
                                                modifier = Modifier
                                                    .padding(horizontal = 10.dp, vertical = 2.dp),
                                                maxLines = 2,
                                                fontSize = 12.sp,

                                                )
                                            Spacer(modifier = Modifier.weight(1f))//no nao weight chiem khoang trang giua 2 cai kia

                                            //box cast
                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color.Black)
                                                    .padding(vertical = 5.dp, horizontal = 8.dp)
                                            ) {
                                                Text(
                                                    text = "${food.price}",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color.White,

                                                    )
                                            }
                                        }

                                    }
                                }
                            }

                        }
                    }


                }
            }
        }

        else -> {}
    }
}

@Preview
@Composable
fun PreviewBg() {
    FeaturedFoodSelection(
        foodState = UiState.Success(listOf()),
        restaurantState = UiState.Success(listOf(Restaurant())) ,
        onClick = {_, _, _ ->},
    )
}