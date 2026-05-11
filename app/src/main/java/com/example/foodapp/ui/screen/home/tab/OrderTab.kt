package com.example.foodapp.ui.screen.home.tab

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.ExpandCircleDown
import androidx.compose.material.icons.rounded.FileCopy
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.core.utils.limit
import com.example.foodapp.core.utils.toFormattedTime
import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Order
import com.example.foodapp.domain.model.OrderStatus
import com.example.foodapp.presentation.extensions.pulseSkeleton
import com.example.foodapp.presentation.extentions.SelectPayment
import com.example.foodapp.presentation.extentions.changeBgStatus
import com.example.foodapp.presentation.extentions.changeIconStatus
import com.example.foodapp.presentation.extentions.changeLineStatus
import com.example.foodapp.presentation.extentions.getTextFromOrderStatus
import com.example.foodapp.presentation.state.OrderUiState
import com.example.foodapp.ui.preview.PreviewDataOrderState
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.Yellow0
import kotlinx.coroutines.delay

@Composable
fun OrderTab(
    orderUiState: OrderUiState,
    orderId: String,
) {
    var detailState by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    var order = Order()
    var loading by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(orderUiState.order) {
        if (orderUiState.order.isNotEmpty()) {
            delay(10000)
            loading = false
        }
    }
    if (loading) {
        LoadingScreen()
        return

    } else {
         order = if (!isPreview) {
            requireNotNull(
                orderUiState.order.find { it.orderId == orderId }
            )
        } else {
            PreviewDataOrderState.previewOrder()
        }
    }
//    if (orderUiState.order.isNotEmpty()) {
//        if (!isPreview) {
//            Log.d("ORDER_CHECK", orderUiState.order.toString())
//            Log.d("ORDER_CHECK", orderId)
//            order = requireNotNull(orderUiState.order.find { it.orderId == orderId })
//        } else {
//            order = PreviewDataOrderState.previewOrder()
//        }
//    } else {
//        LoadingScreen()
//    }





    Scaffold(
        topBar = {
            TopBarOrderDetail()
        },
        containerColor = Gray100
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                val composition by rememberLottieComposition(
                    LottieCompositionSpec.Asset("loading_blue.json")
                )
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever
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
                        Image(
                            painter = painterResource(R.drawable.bg_driver1),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )



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
                                text = getTextFromOrderStatus(order.status),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = if (order.driverId != null && order.driverName != null) {
                                    order.driverName
                                } else {
                                    "Đang tìm tài xế"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Blue0
                            )
                        }
                        if (order.driverId == null || order.driverName == null) {
                            LottieAnimation(
                                composition = composition,
                                progress = { progress },
                                modifier = Modifier.size(100.dp)
                            )
                        } else {
                            Spacer(Modifier.height(10.dp))
                        }


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
                                                order.status,
                                                OrderStatus.CONFIRMED
                                            ),
                                        )
                                )
//                                Box(
//                                    modifier = Modifier
//                                        .width(30.dp)
//                                        .height(3.dp)
//                                        .background(
//                                            Gray100
//                                        )
//                                )

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
                                                order.status,
                                                OrderStatus.PREPARING
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
                                            order.status,
                                            OrderStatus.DELIVERING
                                        ),
                                        shape = CircleShape
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
                                                order.status,
                                                OrderStatus.DELIVERING
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


            item {
                Box(
                    modifier = Modifier
                        .background(
                            Color.White, RoundedCornerShape(
                                30.dp
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Đơn hàng",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp

                            )

                            Spacer(Modifier.weight(1f))
                            Text(
                                modifier = Modifier,
                                text = orderId.limit(15),
                                color = Color.Black.copy(alpha = 0.6f),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )

                            //copy
                            Icon(
                                imageVector = if (true) Icons.Rounded.ContentCopy else Icons.Rounded.FileCopy,
                                contentDescription = null,
                                tint = Blue1
                            )
                        }

                        //time
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Thời gian",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )

                            Spacer(Modifier.weight(1f))
                            Text(
                                modifier = Modifier,
//                                text = "13:35 • 13/4/2026",
                                text = order.createdAt.toFormattedTime(),
                                color = Color.Black.copy(alpha = 0.4f),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        //address
                        Column() {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Rounded.ExpandCircleDown,
                                        contentDescription = null,
                                    )
                                    //line
                                    Box(
                                        Modifier
                                            .height(70.dp)
                                            .width(1.dp)
                                            .background(Gray85)
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text(
                                        text = if (isPreview) "Xôi cô thảo" else order.restaurantName,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black.copy(alpha = 0.7f),
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = if (isPreview) "Xôi cô thảo" else order.restaurantAddress,
                                        color = Color.Black.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = null,
                                        tint = Yellow0
                                    )
                                }
                                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                    Text(
                                        text = if (isPreview) "Cổng 2 đường lê trọng tấn, Ấp tân kỳ phan phú nhuận - Cổng 2 đường lê trọng tấn, Ấp tân kỳ phan phú nhuận"
                                        else order.userAddress,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black.copy(alpha = 0.7f),
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = if (isPreview) "Hung • 012901290 • 0.45km " else "${order.userName} • ${order.userPhoneSnapshot} • 0.4km",
                                        color = Color.Black.copy(alpha = 0.4f),
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }


                        }


                    }
                }
            }

            //
            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(30.dp))
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),

                        ) {
                        Text(
                            text = "Tóm tắt đơn",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "${order.items.size} món",
                                color = Color.Black.copy(alpha = 0.4f),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                            )

                            Text(
                                text = "•",
                                fontSize = 16.sp,
                                color = Color.Black.copy(alpha = 0.4f),
                                fontWeight = FontWeight.SemiBold,
                            )

                            Text(
                                text = order.total.toVND(),
                                color = Color.Black.copy(alpha = 0.8f),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "•",
                                fontSize = 16.sp,
                                color = Color.Black.copy(alpha = 0.4f),
                                fontWeight = FontWeight.SemiBold,
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                SelectPayment(order.paymentMethod)
                                Text(
                                    text = order.paymentMethod.displayName,
                                    color = Color.Black.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                                )
                            }
                        }


                        Spacer(Modifier.height(1.dp))
                        Text(
                            text = if (!detailState) "Xem chi tiết hóa đơn" else "Ẩn bớt",
                            color = Blue1,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            modifier = Modifier.clickable(
                                onClick = {
                                    detailState = !detailState
                                }
                            )
                        )

                        if (!detailState) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                order.items.forEach { item ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {

                                        AsyncImage(
                                            model = item.imgUrl,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clip(RoundedCornerShape(15.dp)),
                                            placeholder = painterResource(R.drawable.bg_box1),
                                            contentScale = ContentScale.Crop
                                        )

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(5.dp),
                                            modifier = Modifier.weight(1f)

                                        ) {
                                            Text(
                                                text = item.foodName,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black.copy(alpha = 0.8f),
                                                fontSize = 15.sp
                                            )

                                            Text(
                                                text = item.selectedOptions.map { it.optionName }
                                                    .joinToString(separator = " | ") { it },
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color.Black.copy(alpha = 0.4f),
                                                fontSize = 14.sp,
                                            )
                                        }

                                        //price
                                        Text(
                                            text = item.price.toVND(),
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.Black.copy(alpha = 0.7F)
                                        )
                                    }
                                }
                            }
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Black.copy(alpha = 0.2f))
                        )

                        Row(

                        ) {
                            Text(
                                text = "Tổng giá món (${order.items.size} món)",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.4f)
                            )

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = order.calculateTotal().toVND(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }

                        Row(

                        ) {
                            Text(
                                text = "Phí giao hàng",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.4f)
                            )

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = order.deliveryFee.toVND(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                        Row(

                        ) {
                            Text(
                                text = "Phí dịch vụ",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.4f)
                            )

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = 4000L.toVND(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                        Row(

                        ) {
                            Text(
                                text = "Tổng thanh toán",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.4f)
                            )

                            Spacer(Modifier.weight(1f))

                            Text(
                                text = order.getTotalPrice().toVND(),
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
//    OrderTab(
//        orderUiState = OrderUiState(),
//        orderId = "sdfs",
//    )
    LoadingScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarOrderDetail() {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(Color.White)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Chi tiết đơn hàng",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            navigationIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,

                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Gray85)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)

    )
}

@Composable
fun LoadingScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(30.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .pulseSkeleton(
                        )
                )



                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Spacer(Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(30.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),

                                )
                    )
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(30.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .clip(CircleShape)
                        .pulseSkeleton(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                )
            }
        }



        Box(
            modifier = Modifier
                .background(
                    Color.White, RoundedCornerShape(
                        30.dp
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(15.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                            )
                    )

                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            )
                    )


                }

                //time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(25.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                    )

                }

                //address
                Column() {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(20.dp)
                                    .clip(CircleShape)
                                    .pulseSkeleton(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                            )
                            //line
                            Box(
                                Modifier
                                    .height(70.dp)

                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .width(250.dp)
                                .height(40.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .background(
                    Color.White, RoundedCornerShape(
                        30.dp
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(15.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                    )

                    Spacer(Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(10.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            )
                    )


                }

                //time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(25.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            )
                    )

                }

                //address
                Column() {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(20.dp)
                                    .clip(CircleShape)
                                    .pulseSkeleton(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                    )
                            )
                            //line
                            Box(
                                Modifier
                                    .height(70.dp)

                            )
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .width(250.dp)
                                .height(40.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                 )
                        )
                    }
                }
            }
        }
    }
}


