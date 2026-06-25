package com.example.foodapp.ui.screen.preview.section

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Yellow0


@Composable
fun HeaderPreviewSelection(
    restaurant: Restaurant
) {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
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
                    text = restaurant.totalRating.toString(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = if (restaurant.totalRating >= index + 1)
                                Yellow0
                            else
                                Gray100
                        )
                    }

                }




                Text(
                    text = "${restaurant.totalReviews} đánh giá",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color.Black.copy(0.5f)
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),

            ) {

            //review count text

            restaurant.reviewsList
                .reversed()
                .forEachIndexed { index, item ->

                    val percent =
                        if (restaurant.totalReviews > 0)
                            item.toFloat() / restaurant.totalReviews
                        else
                            0.0f
                    Log.d("check_percent_preview", "size = ${restaurant.totalReviews}")
                    Log.d("check_percent_preview", "dùng hàm: ${item.toFloat()} / ${restaurant.totalReviews} = $percent")
                    Log.d("check_percent_preview", "ko dùng hàm: ${item} / $3 = ${item/3}")

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        Text(
                            text = "${5 - index} (${item})",
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

                            //box color rating yellow
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(percent)
                                    .height(5.dp)
                                    .background(
                                        Yellow0,
                                        RoundedCornerShape(30.dp)
                                    )
                            )

//                            if (item == 5) {
//
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .height(5.dp)
//                                        .background(
//                                            Yellow0,
//                                            RoundedCornerShape(30.dp)
//                                        )
//                                )
//                            }
                        }

                    }


                }
        }

    }
}



