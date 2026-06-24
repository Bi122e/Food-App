package com.example.foodapp.ui.screen.main.notification

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R
import com.example.foodapp.domain.model.AppNotification
import com.example.foodapp.domain.model.NotificationType
import com.example.foodapp.ui.screen.main.notification.section.ContentNotificationSection
import com.example.foodapp.ui.screen.main.notification.section.EmptyNotification
import com.example.foodapp.ui.screen.main.notification.section.HeaderNotificationSection
import com.example.foodapp.ui.screen.main.notification.section.NotificationHeaderSection
import java.util.Date


@Composable
fun NotificationTab(
    notifications: List<AppNotification>,
    onNavigationToCompleteTab: (orderId: String, notificationId: String) -> Unit,
    onResetToRead: (orderId: String) -> Unit,
) {


    Scaffold(
        topBar = { TopNotification() },
        containerColor = Color.White
    ) { paddingValues ->

        val items = listOf("Tất cả", "Ưu đãi", "Cập nhật", "Phản hồi")
        var selected by remember { mutableStateOf(0) }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {

            item {
                NotificationHeaderSection(
                    notificationsCount = notifications.size
                )
            }
            item {
                Spacer(Modifier.height(20.dp))
            }

            item {
                HeaderNotificationSection(
                    items = items,
                    onSelectedItem = {
                        selected = it
                    },
                    selected = selected,
                )
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {


                when (selected) {

                    0 -> {

                        if (notifications.isNotEmpty()) {

                            ContentNotificationSection(
                                notifications = notifications,
                                onNavigationToCompleteTab = onNavigationToCompleteTab,
                                onResetToRead = onResetToRead,
                            )
                        } else {
                            EmptyNotification()
                        }

                    }

                    1 -> {
                        EmptyNotification("Hiện chưa cập nhật")
                    }

                    2 -> {
                        EmptyNotification("Hiện chưa cập nhật")
                    }

                    3 -> {
                        EmptyNotification("Hiện chưa cập nhật")
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNotification() {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Thông báo",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Preview
@Composable
fun NotificationTabPreview() {
    NotificationTab(
        notifications = listOf(
            AppNotification(
                name = "Món ăn đang chờ bạn đánh giá",
                message = "Đánh giá để có ưu đãi hấp dẫn nhất",
                type = NotificationType.PROMOTION.name,
                createdAt = Date(
                    System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L
                )

            ),
            AppNotification(
                name = "Món ăn đang chờ bạn đánh giá",
                message = "Đánh giá để có ưu đãi hấp dẫn nhất",
                type = NotificationType.ORDER_NEED_RATING.name,
                createdAt = Date(
                    System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L
                ),
                read = true
            )
        ),
        onNavigationToCompleteTab = {_, _ ->},
        onResetToRead = {},
    )
}