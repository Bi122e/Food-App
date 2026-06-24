package com.example.foodapp.ui.screen.main.notification.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coloredShadow

@Composable
fun HeaderNotificationSection(
    items: List<String>,
    onSelectedItem: (Int) -> Unit,
    selected: Int,
) {

    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth()
    ) {
        itemsIndexed(items = items) { index, item ->
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .clickable {
                        onSelectedItem(index)
                    }
                    .coloredShadow(
                        colors = listOf(Color.Black),
                        alpha = 0.1f,
                        borderRadius = 30.dp,
                        blurRadius = 5.dp

                    )
                    .background(
                        if (selected == index)
                            Color.Black
                        else
                            Color.White,
                        RoundedCornerShape(30.dp)
                    )
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = item,
                    color =
                        if (selected == index)
                            Color.White
                        else
                            Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}