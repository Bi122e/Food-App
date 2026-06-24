package com.example.foodapp.ui.screen.main.restaurant.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.ui.theme.MediumGray

@Composable
fun HeaderRestaurantUI(onClickBackHome: () -> Unit) {
    val isPreview = LocalInspectionMode.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .background(
                    color = Color.Black.copy(alpha = 0.35f),
                    shape = CircleShape,
                )
                .padding(6.dp)
                .clickable { onClickBackHome() }
        )

        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .background(MediumGray, RoundedCornerShape(45.dp)),
            value = "",
            onValueChange = {},
            singleLine = true,
            textStyle = TextStyle(fontSize = 13.sp, color = Color.White),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                    Box(Modifier.weight(1f)) {
                        // Placeholder
                        Text(
                            "Tìm kiếm",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        innerTextField()
                    }
                }
            }
        )

        Icon(
            imageVector = Icons.Rounded.IosShare,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .background(
                    color = Color.Black.copy(0.35f),
                    shape = CircleShape
                )
                .padding(6.dp)
        )

        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            tint = Color.White,
            contentDescription = null,
            modifier = Modifier
                .background(
                    color = Color.Black.copy(0.35f),
                    shape = CircleShape
                )
                .padding(6.dp)
        )
    }
}