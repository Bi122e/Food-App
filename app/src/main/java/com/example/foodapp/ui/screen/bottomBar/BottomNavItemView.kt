package com.example.foodapp.ui.screen.bottomBar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.domain.model.BottomNavItem

@Composable
fun BottomNavItemView(
    item: BottomNavItem,
    selected: Boolean,
    onCLick: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    val backgroundColor = if (selected) Color(0xFFE3F2FD) else Color.Transparent
    val contentColor = if (selected) Color(0xFF1E88E5) else Color.Gray

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(40.dp))
            .background(backgroundColor)
            .clickable {onCLick()}
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = contentColor
        )
        Text(
            text = item.title,
            fontSize = 29.sp,
            color = contentColor
        )
    }
}
