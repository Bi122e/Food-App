package com.example.foodapp.ui.screen.main.notification.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R


@Composable
fun EmptyNotification(
    message: String = "Hiện không có thông báo"
) {

    Box() {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 80.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)

        ) {
            Image(
                painter = painterResource(R.drawable.ic_empty_message1),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = message,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = Color.Black.copy(0.4f)
            )

        }
    }
}

@Preview
@Composable
fun EmptyNotificationPreview() {

    EmptyNotification(message = "test")
}