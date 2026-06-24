package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NavBottomBarProfileSection() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFfbf9ed),
                RoundedCornerShape(15.dp)
            )
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = Color(0xFFd5a82d),
                    modifier = Modifier
                        .size(28.dp)
                )

                Text(
                    text = "Thanh điều hướng",
                    color = Color(0xFFd5a82d),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Bạn có thể thay đổi tùy chọn giao diện thanh điều hướng",
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )

                Icon(
                    Icons.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.Black,
                )
            }

        }
    }
}