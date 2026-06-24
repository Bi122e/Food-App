package com.example.foodapp.ui.screen.main.order.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Order
import com.example.foodapp.presentation.extentions.SelectPayment
import com.example.foodapp.ui.theme.Blue1

@Composable
fun SummaryOrderSection(
    order: Order,
    detailState: Boolean,
    onChangeDetailState: () -> Unit,
) {

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
                text = "Tóm tắt đơn", fontWeight = FontWeight.SemiBold, fontSize = 16.sp
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
                        onChangeDetailState()
                    }
                )
            )

            if (detailState) {
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.2f))
                )

                Row(

                ) {
                    Text(
                        text = "Tổng giá món (${order.items.size} món • SL ${order.items.sumOf { it.quantity }})",
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