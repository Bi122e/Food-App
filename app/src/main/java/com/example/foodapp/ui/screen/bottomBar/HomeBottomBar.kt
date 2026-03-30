package com.example.foodapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)


@Composable
fun HomeBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    badgeProfile: Boolean,
) {
    val items = listOf(
        BottomNavItem("Trang chủ", Icons.Default.People),
        BottomNavItem("Chat", Icons.Default.Chat),
        BottomNavItem("Giỏ hàng", Icons.Default.ShoppingCartCheckout),
        BottomNavItem("Hồ sơ", Icons.Default.Person),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 30.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(40.dp)
                )
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White)
                .padding(vertical = 4.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            //loop for item in list,
            items.forEachIndexed { index, item ->
                BottomNavItemView(
                    item = item,
                    selected = selectedIndex == index, //selected Idx ==
                    onClick = { onItemSelected(index) },
                    modifier = Modifier.weight(1f),
                    badgeProfile = badgeProfile
                )
            }
        }
    }
}


@Composable
private fun BottomNavItemView(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeProfile: Boolean,
) {
    val backgroundColor = if (selected) {
        Color(0xFF1E88E5).copy(alpha = 0.1f)
    } else {
        Color.Transparent
    }

    val contentColor = if (selected) {
        Color(0xFF1E88E5)
    } else {
        Color(0xFF9E9E9E)
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier) {
            if (item.title == "Hồ sơ" && !selected && badgeProfile) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    modifier = Modifier
                        .size(14.dp)
                        .offset {IntOffset(x = 60, y = -20)},
                    contentDescription = null,
                    tint = Color.Red)
            }

            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.title,
            fontSize = 10.sp,
            color = contentColor
        )
    }
}