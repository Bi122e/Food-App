package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Wallet
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
fun PaymentProfileSection(
    modifier: Modifier,
    showSnackBar: () -> Unit,
) {

    Box(
        modifier = modifier
            .size(90.dp)
            .clickable(onClick = showSnackBar)
            .coloredShadow(
                colors = listOf(Gray65),
                alpha = 0.5f,
                borderRadius = 20.dp,
                blurRadius = 3.dp
            )
            .background(
                Color.White,
                RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                5.dp,
                Alignment.CenterVertically
            ),
            modifier = androidx.compose.ui.Modifier
                .padding(6.dp)
                .fillMaxHeight(),

            ) {

            Icon(
                imageVector = Icons.Rounded.Wallet,
                contentDescription = null,
                tint = Color.Blue.copy(0.5f),
                modifier = androidx.compose.ui.Modifier
                    .size(32.dp)
            )

            Text(
                text = "Thanh toán",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(0.7f)
            )
        }
    }

}