package com.example.foodapp.ui.screen.main.order.section

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.ExpandCircleDown
import androidx.compose.material.icons.rounded.FileCopy
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.core.utils.limit
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.core.utils.toFormattedTime
import com.example.foodapp.domain.model.Order
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.Yellow0
import kotlinx.coroutines.delay

@Composable
fun InformationOrderSection(
    order: Order,
) {

    val isPreview = LocalInspectionMode.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var isCopy by remember { mutableStateOf(false) }

    LaunchedEffect(clipboardManager) {
        if (isCopy) {
            delay(2000)
            isCopy = true
        }
    }

    Box(
        modifier = Modifier.background(
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Đơn hàng",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp

                )

                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier,
                    text = order.orderId.limit(15),
                    color = Color.Black.copy(alpha = 0.6f),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                //copy
                Icon(
                    imageVector = if (!isCopy) Icons.Rounded.ContentCopy else Icons.Rounded.FileCopy,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = {
                            isCopy = true
                            clipboardManager.setText(
                                AnnotatedString(order.orderId)
                            )

                            showToast(context, "Đã copy Mã đơn hàng")
                        }),
                    tint = Blue1)
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


@Preview
@Composable
fun InformationOrderSectionPreview() {
    Box(Modifier.fillMaxSize().background(Color.White))
    InformationOrderSection(
        order = Order()
    )
}