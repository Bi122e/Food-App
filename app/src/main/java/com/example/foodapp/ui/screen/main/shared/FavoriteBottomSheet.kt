package com.example.foodapp.ui.screen.main.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.domain.model.Restaurant
import coloredShadow
import com.example.foodapp.ui.preview.PreviewDataRestaurant
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Blue4
import com.example.foodapp.ui.theme.BrightOrange
import com.example.foodapp.ui.theme.Brow0
import com.example.foodapp.ui.theme.Gray65

@ExperimentalMaterial3Api
@Composable
fun FavoriteBottomSheet(
    onDismiss: () -> Unit,
    restaurant: Restaurant,
) {


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        contentColor = Color.White,
        containerColor = Color.White,
//        contentWindowInsets = WindowInsets(top = 20.dp)
         modifier = Modifier.padding(vertical = 60.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(30.dp)


    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.CenterStart)
                )

                Text(
                    text = "Lưu quán yêu thích",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black.copy(0.1f))
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(100.dp)
//                    .shadow(
//                        elevation = 4.dp,
//                        RoundedCornerShape(20.dp),
//                        clip = false
//                    )
                    .coloredShadow(
                        colors = listOf(Gray65)
                    )
                    .clip(RoundedCornerShape(20.dp))

                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 6.dp, horizontal = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)

                ) {
                    AsyncImage(
                        model = "",
                        placeholder = painterResource(R.drawable.bg_box1),
                        error = painterResource(R.drawable.bg_box1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                             .clip(RoundedCornerShape(20.dp)),
                    )

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
                                modifier = Modifier.width(100.dp),
                                text = restaurant.restaurantName,
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
                                    imageVector = Icons.Rounded.Route,
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "3.2 km",
                                    color = Color.Black.copy(0.5f),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }

                    }
                }
            }

            //lazy
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.Cyan.copy(0.1f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Rounded.Favorite,
                                contentDescription = null,
                                tint = Color.Red.copy(0.4f),
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Column() {
                            Text(
                                text = "Yêu thích",
                                color = Color.Black
                            )
                            Text(
                                text = "0 quán",
                                color = Color.Black
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    color = Blue0,
                                    RoundedCornerShape(7.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.Check,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            Spacer(Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Black.copy(0.1f))
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Surface(
                    onClick = {},
                    shape = CircleShape,
                    contentColor = Blue1,
                    border = BorderStroke(
                        1.dp, Blue1
                    ),
                    color = Blue2.copy(0.1f),
                ) {

                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = null,
                        Modifier
                            .padding(10.dp)
                            .size(32.dp)
                    )
                }
                Text(
                    text = "Thêm bộ sưu tập mới",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(10.dp))

            //btn
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        Blue4,
                        RoundedCornerShape(30.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Lưu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewFavoriteBottomSheet() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

    }
    FavoriteBottomSheet(
        onDismiss = {},
        restaurant = PreviewDataRestaurant.restaurant
    )
}
