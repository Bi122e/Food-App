package com.example.foodapp.ui.screen.main.notification.section

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R
import com.example.foodapp.core.utils.timeAgo
import com.example.foodapp.domain.model.AppNotification
import com.example.foodapp.domain.model.NotificationType
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.BlueGreen


@Composable
fun ContentNotificationSection(
    notifications: List<AppNotification>,
    onNavigationToCompleteTab: (orderId: String, notificationId: String) -> Unit,
    onResetToRead: (orderId: String) -> Unit,
) {

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        notifications.forEach { notification ->
            val isOrderRating = notification.type == NotificationType.ORDER_NEED_RATING.name
            Log.d("check_orderId_ui_ContentNotificationSection", "Loop: ${notification.id}")
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {

                        when (notification.type) {
                            NotificationType.ORDER_NEED_RATING.name -> {
                                Log.d("check_orderId_ui_ContentNotificationSection", "${notification.id}")
                                onNavigationToCompleteTab(
                                    notification.payload["orderId"] ?: "...",
                                    notification.id
                                    )
                                onResetToRead(notification.id)
                            }
                            else -> {
                                //do some things
                                onResetToRead(notification.id)
                            }
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = if (isOrderRating)
                        painterResource(R.drawable.ic_review1)
                    else
                        painterResource(R.drawable.ic_mobile1),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Blue1.copy(0.1f),
                            CircleShape
                        )
                        .padding(10.dp)
                )


                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isOrderRating)
                                "Món ăn đang chờ bạn đánh giá"
                            else
                                "Chào mừng bạn đã đăng ký thành công",
                            modifier = Modifier.weight(7f),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,

                            )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = notification.createdAt?.timeAgo() ?: "---",
                            color = Color.Black.copy(0.4f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                     ) {
                        Text(
                            text = if (isOrderRating)
                                "Đánh giá để có ưu đãi hấp dẫn nhất"
                            else
                                "hãy cập nhật đầy đủ thông tin cá nhân để dễ dàng đặt món",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(4f),
                            fontSize = 14.sp,
                            color = Color.Black.copy(0.5F)
                        )

                        Spacer(Modifier.weight(1f))

                        if (!notification.read) {
                            Box(
                                modifier = Modifier
                                     .size(12.dp)
                                    .background(
                                        Blue1,
                                        CircleShape
                                    )
                            )
                        }
                    }



                    Spacer(Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(7.dp),
                            )
                            .background(
                                Color(0xFFddf6f9),
                            )
                            .padding(vertical = 6.dp, horizontal = 12.dp)
                    ) {

                        Text(
                            text = if (isOrderRating)
                                "Đánh giá ngay!"
                            else
                                "Thực hiện ngay!",
                            color = BlueGreen,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}