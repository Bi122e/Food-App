package com.example.foodapp.ui.screen.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun RestaurantTabRow(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Tất cả", "Bán chạy", "Yêu thích")

    TabRow(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp)),
        selectedTabIndex = selectedIndex,
        containerColor = Color.White,
        indicator = { tabPositions ->

            //box den
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .padding(4.dp)
                    .fillMaxSize()
                    .zIndex(-1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black)
            )
        },
        divider = {} // delete duong ke mac dinh
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier.height(45.dp),
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black,
                text = {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = if (index == selectedIndex) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}