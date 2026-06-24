package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R

@Composable
fun SettingProfileSection(

) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Cài đặt chung",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(start = 16.dp)
        ) {

            Icon(
                painterResource(R.drawable.ic_language),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Ngôn ngữ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Spacer(Modifier.weight(1f))

            Icon(
                Icons.Rounded.ArrowForwardIos,
                contentDescription = null
            )
        }
    }
}