package com.example.foodapp.ui.screen.preview.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Star
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
import com.example.foodapp.ui.theme.Yellow0


@Composable
fun HeaderContentPreviewSelection(
    onSelectedTag: (String) -> Unit
) {


    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Đánh giá và bình luận",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Box(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .coloredShadow(
                        colors = listOf(
                            Color.Black
                        ),
                        alpha = 0.5f,
                        borderRadius = 30.dp,
                        blurRadius = 0.4.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Có ảnh/Bình luận",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = Icons.Rounded.Star,
                    tint = Color.Transparent,
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 2.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .coloredShadow(
                        colors = listOf(
                            Color.Black
                        ),
                        alpha = 0.5f,
                        borderRadius = 30.dp,
                        blurRadius = 0.4.dp
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {

                    Text(
                        text = "Số sao",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        tint = Yellow0,
                        contentDescription = null
                    )

                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        modifier = Modifier.size(24.dp),
                        contentDescription = null
                    )
                }

            }
        }
    }
}