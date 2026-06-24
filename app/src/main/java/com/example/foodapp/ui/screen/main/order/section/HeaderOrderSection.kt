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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.presentation.extentions.changeBgStatus
import com.example.foodapp.presentation.extentions.changeIconStatus
import com.example.foodapp.presentation.extentions.changeLineStatus
 import com.example.foodapp.ui.theme.Blue0

@Composable
fun HeaderOrderSection(
    order: Order,

) {
    val isPreview = LocalInspectionMode.current

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("loading_blue.json")
    )
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {

            Box(
                contentAlignment = Alignment.TopEnd
            ) {

                Image(
                    painter = painterResource(R.drawable.bg_driver1),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                if (order.isDriverAssigned) {
                    Image(
                        painter = painterResource(R.drawable.ic_check2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = (10).dp)
                    )
                }
            }



            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Không để bạn chờ lâu",
                    color = Color.Black.copy(alpha = 0.4f),
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    text = order.status.vietnameseLabel,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue0
                )
                if (!order.isDriverAssigned) {
                    Text(
                        text = order.driverName ?: "Đang tìm tài xế",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }
            if (!order.isDriverAssigned) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(100.dp)
                )
            } else {
                Spacer(Modifier.height(10.dp))
            }
5

            //step process
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_order1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = changeBgStatus(order.status, OrderStatus.CONFIRMED),
                            shape = CircleShape
                        )
                        .padding(5.dp),
                    tint = changeIconStatus(order.status, OrderStatus.CONFIRMED)
                )

                //line
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(3.dp)
                            .background(
                                color = changeLineStatus(
                                    order.status, OrderStatus.CONFIRMED
                                ),
                            )
                    )
                }

                Icon(
                    painter = painterResource(R.drawable.ic_fork_knife),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = changeBgStatus(order.status, OrderStatus.PREPARING),
                            shape = CircleShape
                        )
                        .padding(5.dp),
                    tint = changeIconStatus(order.status, OrderStatus.PREPARING)
                )
                //line
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(3.dp)
                            .background(
                                color = changeLineStatus(
                                    order.status, OrderStatus.PREPARING
                                ),
                            )
                    )

                }
                Icon(
                    painter = painterResource(R.drawable.ic_motor),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = changeBgStatus(
                                order.status, OrderStatus.DELIVERING
                            ), shape = CircleShape
                        )
                        .padding(5.dp),
                    tint = changeIconStatus(order.status, OrderStatus.DELIVERING)
                )

                //line
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(3.dp)
                            .background(
                                color = changeLineStatus(
                                    order.status, OrderStatus.DELIVERING
                                ),
                            )
                    )

                }

                Icon(
                    painter = painterResource(R.drawable.ic_home1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            changeBgStatus(order.status, OrderStatus.DELIVERED),
                            shape = CircleShape
                        )
                        .padding(5.dp),
                    tint = changeIconStatus(order.status, OrderStatus.DELIVERED)
                )

            }
        }
    }
}

@Preview
@Composable
fun HeaderOrderSectionPreview() {
    Box(Modifier.fillMaxSize().background(Color.White))
    HeaderOrderSection(
        order = Order()
    )
}
