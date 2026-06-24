package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Yellow3

@Composable
fun StatisticalProfileSection() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Yellow3.copy(alpha = 0.2f), Blue1.copy(alpha = 0.5f)
                    )
                )
            )
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row {
                Box {
                    Image(
                        painter = painterResource(R.drawable.bg_rec_card),
                        contentDescription = null,
                        modifier = Modifier.width(200.dp)
                    )

                    Text(
                        text = "GOLDEN FLAVOR",
                        modifier = Modifier.padding(
                            vertical = 10.dp,
                            horizontal = 16.dp
                        ),
                        fontSize = 16.sp,
                        color = Color(0xFF306345).copy(alpha = 0.6f),
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .padding(
                            vertical = 10.dp, horizontal = 10.dp
                        )
                        .background(
                            Color(0xFFECFFFE), RoundedCornerShape(10.dp)
                        ), contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(
                            vertical = 7.dp,
                            horizontal = 10.dp
                        ),

                        ) {
                        Text(
                            text = "Xem tiến trình", color = Color(0xFF119DC4)
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF119DC4)
                        )
                    }
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(start = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.bg_cutlery),
                        contentDescription = null
                    )
                    Text(
                        text = "Bậc thầy gọi món",
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                color = Color(0xFF8DB28A), RoundedCornerShape(30.dp)
                            )
                            .padding(vertical = 5.dp, horizontal = 10.dp)
                    )
                }

                Spacer(Modifier.weight(1f))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Điểm tích lũy",
                        color = Color(0xFF0E6A84),
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFF6D98B),
                                        Color(0xFFFFF0C9),
                                        Color(0xFFF3D27B)
                                    ),
//                                    start = Offset.Zero,
//                                    end = Offset.Infinite
                                )
                            ), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "0",
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFB07D30)
                        )
                    }
                }
            }

            Box(
                Modifier
                    .height(1.dp)
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tổng số xu\nđã tích lũy",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "1",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A5302)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Tổng món\nđã đặt",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "100.000",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A5302)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Món tích lũy\nnăm 2026",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        text = "100.000",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = Color(0xFF1A5302)
                    )
                }
            }
        }
    }
}