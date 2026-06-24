package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coloredShadow
import com.example.foodapp.ui.theme.Gray65

@Composable
fun AddressProfileSection(
    modifier: Modifier,
) {

    //dia chi
    Box(
        modifier = modifier
            .size(90.dp)
            .coloredShadow(
                colors = listOf(Gray65) ,
                alpha = 0.5f,
                borderRadius = 20.dp,
                blurRadius = 3.dp
            )
            .background(
                Color.White,
                RoundedCornerShape(20.dp)
            ),

        ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                5.dp,
                Alignment.CenterVertically
            ),
            modifier = androidx.compose.ui.Modifier
                .padding(start = 25.dp)
                .fillMaxHeight()
        ) {

            Icon(
                imageVector = Icons.Rounded.LocationOn,
                contentDescription = null,
                tint = Color.Magenta.copy(0.5f),
                modifier = androidx.compose.ui.Modifier
                    .size(32.dp)
            )

            Text(
                text = "Địa chỉ",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(0.7f)
            )
        }
    }
}