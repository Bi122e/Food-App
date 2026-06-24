package com.example.foodapp.ui.screen.main.checkout

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Cart
import com.example.foodapp.domain.model.CartItem
 import com.example.foodapp.domain.model.PaymentMethod
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.domain.model.User
import com.example.foodapp.domain.model.VariationOption
import com.example.foodapp.presentation.state.CheckoutUiState
import com.example.foodapp.ui.preview.PreviewCheckoutState
import com.example.foodapp.ui.screen.main.food.topShadow
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Gray65
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun CheckOutTab(
    checkoutUiState: CheckoutUiState,
    increaseQty: (
        foodId: String, variations: Map<String, List<VariationOption>>, quantity: Int
    ) -> Unit,
    decreaseQty: (
        foodId: String, variations: Map<String, List<VariationOption>>, quantity: Int
    ) -> Unit,
    onNavCash: () -> Unit,
    onOrderClick: (payment: PaymentMethod) -> Unit,
) {
    val cart = checkoutUiState.cart ?: Cart()
    val restaurant = checkoutUiState.restaurant ?: Restaurant()
    val user = checkoutUiState.user ?: User()
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current
    Log.d("CheckoutViewModel observeCart", "state UI: ${cart}")

    Scaffold(

        topBar = {
            TopBarCheckOut(
                restaurant = restaurant
            )
        },
        bottomBar = {
            BottomOrder(
                onOrderClick = onOrderClick,
                checkoutUiState = checkoutUiState,
            )

        },
        containerColor = Color.White
    ) { paddingValues ->

        if (cart.cartItems.isNotEmpty()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 20.dp,
                    bottom = paddingValues.calculateTopPadding() + 20.dp

                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {

                //info
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        color = Color.White,
                        tonalElevation = 1.dp,
                        border = BorderStroke(1.dp, Gray85)
                    ) {
                        //info
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                        ) {

                            //info
                            Text(
                                text = "Thông tin", fontWeight = FontWeight.SemiBold
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    Icons.Rounded.LocationOn,
                                    contentDescription = null,
                                    tint = Yellow0
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                ) {

                                    //address
                                    Text(
                                        text = if (isPreview) "Ấp 1 đường Lê Thánh Tôn, Quận Bình Phước, Thành Phố Quận Bình Phước, Thành Phố"
                                        else user.address,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    //ten + sdt
                                    Text(
                                        text = if (isPreview) "my name | +841210298" else "${user.name} | ${user.phone}",
                                        color = Gray65
                                    )


                                }

                                // edit onclick
                                Surface(
                                    shape = RoundedCornerShape(30.dp),
                                    border = BorderStroke(1.dp, Color.Cyan),
                                    color = Blue2.copy(alpha = 0.3f)
                                ) {

                                    Text(
                                        modifier = Modifier.padding(8.dp),
                                        text = "Sửa",
                                        color = Blue0,
                                        fontSize = 12.sp
                                    )

                                }
                            }

                            //line
                            Box(
                                modifier = Modifier
                                    .padding(start = 35.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Gray85)
                            )


                            //tien mat
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.clickable(
                                    onClick = {
                                        showToast(context, "clicked")
                                        onNavCash()
                                    }
                                ),
                            ) {

                                Icon(
                                    Icons.Default.Wallet,
                                    contentDescription = null,
                                    tint = Color.Black
                                )

                                Text(
                                    text = checkoutUiState.paymentMethod.displayName,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Icon(
                                    Icons.Default.ArrowForwardIos,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Gray65
                                )
                            }

                            //time delivery
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    Icon(
                                        Icons.Default.AccessTime, contentDescription = null
                                    )

                                    Text(
                                        text = "Giao nhanh",
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Text(
                                    text = "1.11km",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(start = 35.dp)
                                )
                            }

                        }
                    }
                }


                //summary
                item {

                    val context = LocalContext.current
                    val isPreview = LocalInspectionMode.current
                    Surface(
                        shape = RoundedCornerShape(30.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Gray85),
                        modifier = Modifier
                            .fillMaxWidth(),

                        ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement
                                .spacedBy(20.dp)
                        ) {

                            //header summary
                            Column(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Tóm tắt đơn",
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(Modifier.weight(1f))

                                    Text(
                                        text = "Thêm món",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Blue0
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isPreview) "2 món" else "${cart.cartItems.size} món",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Gray65
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Circle,
                                        contentDescription = null,
                                        tint = Gray65,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = "Chỉnh sửa món",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Blue0,
                                    )

                                    Spacer(Modifier.weight(1f))

                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        modifier = Modifier.size(38.dp),
                                        contentDescription = null,
                                        tint = Gray65
                                    )
                                }

                            }


                            //food info
                            val display = if (isPreview) {
                                PreviewCheckoutState.previewCheckout.cart?.cartItems ?: listOf(
                                    CartItem()
                                )
                            } else {
                                cart.cartItems
                            }

                            display.forEachIndexed { idx, cartItem ->

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                                    modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)
                                ) {
                                    //row 1 avatar
                                    AsyncImage(
                                        model = cartItem.imgUrls,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(20.dp)),
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(R.drawable.bg_box1),
                                        error = painterResource(R.drawable.bg_error1),
                                    )


                                    //row 2 food name + option
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(5.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = if (isPreview) "Bún lem lướng" else cartItem.name,
                                        )
                                        Text(
                                            text = cartItem.variation.flatMap {
                                                    it.value.map { option ->
                                                        option.name
                                                    }
                                            }.joinToString(separator = " | ") { it },
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 12.sp,
                                            color = Gray65
                                        )

                                        Spacer(Modifier.weight(1f))
                                        //note
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
//                                        .align(alignment = Alignment.BottomStart)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Comment,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp),
                                                tint = Gray65
                                            )

                                            Text(
                                                text = "Ghi chú cho quán",
                                                color = Gray65,
                                                fontSize = 11.sp
                                            )
                                        }

                                    }

                                    //row 3 (them xoa sua)
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier
//                                    .height(IntrinsicSize.Min)
                                            .fillMaxHeight()
                                    ) {

                                        //price
                                        Text(
                                            text = if (isPreview) "34.000d" else cartItem.getTotalPrice()
                                                .toVND(),
                                            fontWeight = FontWeight.SemiBold,
//                                    modifier = Modifier.align(Alignment.End)                                )
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        //them - xoa
                                        Surface(
                                            border = BorderStroke(width = 1.dp, color = Color.Cyan),
                                            shape = RoundedCornerShape(15.dp),
                                            color = Blue2.copy(alpha = 0.3f),
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,

                                                ) {
                                                //giam
                                                Icon(
                                                    imageVector = Icons.Default.Remove,
                                                    contentDescription = null,
                                                    tint = Blue0,
                                                    modifier = Modifier
                                                        .clickable(
                                                            enabled = true,
                                                            onClick = {
                                                                decreaseQty(
                                                                    cartItem.foodId,
                                                                    cartItem.variation,
                                                                    cartItem.quantity
                                                                )
                                                            }
                                                        )
                                                        .padding(
                                                            horizontal = 6.dp,
                                                            vertical = 4.dp
                                                        )

                                                )
                                                Text(
                                                    modifier = Modifier.padding(horizontal = 5.dp),
                                                    text = if (isPreview) "1" else cartItem.quantity.toString(),
                                                    fontSize = 16.sp
                                                )

                                                //tang
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = null,
                                                    tint = Blue0,
                                                    modifier = Modifier
                                                        .clickable(
                                                            enabled = true,
                                                            onClick = {
                                                                increaseQty(
                                                                    cartItem.foodId,
                                                                    cartItem.variation,
                                                                    cartItem.quantity
                                                                )
                                                            }
                                                        )
                                                        .padding(
                                                            horizontal = 6.dp,
                                                            vertical = 4.dp
                                                        ),
                                                )
                                            }
                                        }
                                    }
                                }
                                //divider
                                if (idx < cart.cartItems.size - 1) {
                                    DashedDivider(
                                        modifier = Modifier.padding(start = 90.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                //chi tiet hoa don
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Gray85)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(15.dp),
//                        modifier = Modifier.padding(vertical = 15.dp)
                            modifier = Modifier.padding(horizontal = 15.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 20.dp, bottom = 5.dp),
                                text = "Chi tiết thanh toán",
                                fontWeight = FontWeight.SemiBold,
                            )

                            //tong gia mon
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isPreview) "Tổng thanh toán (3 món)" else "Tổng thanh toán (${cart.cartItems.size} món)",
                                    color = Gray65
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = if (isPreview) "34.000d" else cart.calculateTotalPrice()
                                        .toVND()
                                )
                            }

                            //phi giao hang
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Phí giao hàng", color = Gray65
                                )
                                Spacer(Modifier.weight(1f))
                                Text(
                                    text = if (isPreview) "16.000d" else restaurant.deliveryFee.toVND()
                                )
                            }

                            //phi dich vu
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Phí dịch vụ", color = Gray65
                                )
                                Spacer(Modifier.weight(1f))

                                Text(
                                    text = "4.000d",
                                )
                            }

                            //tong thanh toan
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 5.dp, bottom = 20.dp)

                            ) {
                                Text(
                                    text = "Tổng thanh toán",
                                )

                                Spacer(Modifier.weight(1f))

                                Text(
                                    text = if (isPreview) "54.000d" else cart.calculateTotalPrice()
                                        .toVND(),
                                    color = Blue0,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 250.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(
                            R.drawable.bg_empty_cart
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(300.dp)
                    )

                    Text(
                        text = "Hiện tại bạn đang không có đơn hàng nào",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray65,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "Đặt ngay",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Blue0,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable(
                                onClick = { showToast(context = context, "Clicked") }
                            )
                    )

                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun ShowPreview() {
    CheckOutTab(
        checkoutUiState = PreviewCheckoutState.previewCheckout,
        increaseQty = { _, _, _ -> },
        decreaseQty = { _, _, _ -> },
        onNavCash = {},
        onOrderClick = {},
    )
}


//top bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCheckOut(
    restaurant: Restaurant
) {
    val isPreview = LocalInspectionMode.current
    Column(

        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            ),
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Thanh toán",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        text = if (isPreview) "Bún thịt nướng Kiều Bảo - Lê Trọng Tấn" else restaurant.restaurantName,
                        color = Color.Black.copy(0.5f),
                        maxLines = 1,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis

                    )


                }

            },

            //back
            navigationIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = null
                )
            })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .background(Gray65.copy(0.1f))
        )
    }

}


//dash divider
@Composable
fun DashedDivider(
    color: Color = Color.Black.copy(1f),
    thickness: Float = 1f,
    dashWidth: Float = 10f,
    gapWidth: Float = 10f,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = thickness,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashWidth, gapWidth), 0f
            )
        )
    }
}

@Composable
fun BottomOrder(
    onOrderClick: (payment: PaymentMethod) -> Unit,
    checkoutUiState: CheckoutUiState,
) {

    val cart = checkoutUiState.cart ?: return

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .topShadow()

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(15.dp)
        ) {

            //tien
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tổng thanh toán", fontWeight = FontWeight.SemiBold, fontSize = 16.sp
                )
                Spacer(Modifier.weight(1f))

                //tong tien
                Text(
                    text = cart.calculateTotalPrice().toVND(),
                    fontWeight = FontWeight.SemiBold,
                    color = Blue0,
                    fontSize = 20.sp
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onOrderClick(checkoutUiState.paymentMethod)
                    }
                    .background(Blue1, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {

                //on click
                Text(
                    text = "Đặt đơn",
                    modifier = Modifier.padding(vertical = 15.dp),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}


