package com.example.foodapp.ui.screen.main.order.section

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.foodapp.R
import com.example.foodapp.domain.model.Order
import com.example.foodapp.ui.theme.DefaultBg1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.White
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun DriverOrderSelection(
    order: Order
) {

//    if (order.isDriverAssigned) {
    if (order.isDriverAssigned) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(30.dp)),
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    SubcomposeAsyncImage(
                        model = order.driverAvatar,
                        contentScale = ContentScale.Crop,
                        error = {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Gray100,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = if (order.genderDriver == "men")
                                        painterResource(R.drawable.ic_boy1)
                                    else
                                        painterResource(R.drawable.ic_girl1),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)

                                )
                            }
                        },
                        loading = {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Gray100,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = if (order.genderDriver == "men")
                                        painterResource(R.drawable.ic_boy1)
                                    else
                                        painterResource(R.drawable.ic_girl1),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)

                                )
                            }
                        },

//                        modifier = Modifier.size(70.dp),
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Color.LightGray)
                    )

                    SubcomposeAsyncImage(
                        model = order.driverAvatar,
                        contentScale = ContentScale.Crop,
                        error = {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Gray100,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_motorcycle1),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)

                                )
                            }
                        },
                        loading = {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Gray100,
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_motorcycle1),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp)

                                )
                            }
                        },

//                        modifier = Modifier.size(70.dp),
                        contentDescription = null
                    )
                }


                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        Text(
                            text = order.driverName ?: "...",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )

                        Icon(
                            imageVector = Icons.Rounded.Circle,
                            contentDescription = null,
                            modifier = Modifier.size(6.dp)
                        )

                        Text(
                            text = order.ratingDriver.toString(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                            tint = Yellow0
                        )
                    }



                    Spacer(modifier = Modifier.weight(1f))


                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.End,
                     ) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color.Black,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(6.dp)
                        ) {


                            Text(
                                text = order.licensePlate,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )

                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                         ) {
                            Text(
                                text = order.vehicleName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )


                            Icon(
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = null,
                                modifier = Modifier
                                     .size(6.dp)
                            )

                            Text(
                                text = order.vehicleColor,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                Gray100,
                                RoundedCornerShape(30.dp)
                            ),
                     ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            modifier = Modifier.padding(horizontal = 26.dp, vertical = 16.dp)
                        ) {

                            Image(
                                painter = painterResource(R.drawable.ic_message1),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = "Nhắn tin với bác tàdfi.",
                                color = Color.Black.copy(0.5f),
                                fontSize = 16.sp
                            )
                        }
                    }

                    Image(
                        painter = painterResource(R.drawable.ic_telephone1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                Gray100,
                                CircleShape
                            )
                            .padding(15.dp),
                    )
                }
            }
        }
    }


}


@Preview
@Composable
fun DriverOrderSelectionPreview() {

    Box(
        Modifier
            .fillMaxSize()
            .background(DefaultBg1)
    )
    DriverOrderSelection(
        order = Order(
            driverId = "dsf",
            driverName = "Nguyen Dinh LIem",
            licensePlate = "9xf -fdfd.09",
            ratingDriver = 4.5,
            vehicleName = "EVO",
            vehicleColor = "Cyan",
         )
    )
}