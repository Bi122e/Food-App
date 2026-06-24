package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import com.example.foodapp.ui.theme.Gray65


@Composable
fun EvaluateProfileSection(

) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White.copy(0.6f),
                RoundedCornerShape(20.dp)
            )
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                Icons.Rounded.Favorite,
                contentDescription = null,
                tint = Color.Red.copy(0.6f),
                modifier = Modifier.size(32.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Bạn có hài lòng ứng dụng chứ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Phản hồi của bạn giúp chúng tôi ngày hoàng thiện hơn",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(0.5f),
                        fontSize = 14.sp
                    )
                }

                Icon(
                    Icons.Rounded.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier
                        .coloredShadow(
                            listOf(Gray65),
                            0.5f,
                            10.dp,
                            4.dp,
                        )
                        .size(34.dp)
                        .background(
                            Color.White,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )

            }
        }
    }
}