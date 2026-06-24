package com.example.foodapp.ui.screen.main.notification.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.ui.theme.Blue0

@Composable
 fun NotificationHeaderSection(
    notificationsCount: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Các thông báo",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 16.sp
        )
        if (notificationsCount > 0) {

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Blue0, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = notificationsCount.toString(),
                    color = Color.White
                )
            }
        }
    }
}