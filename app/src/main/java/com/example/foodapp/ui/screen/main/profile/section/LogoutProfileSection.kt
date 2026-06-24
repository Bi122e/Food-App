package com.example.foodapp.ui.screen.main.profile.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R

@Composable
fun LogoutProfileSection(
    onLogout: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            10.dp,
            Alignment.CenterHorizontally
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onLogout()
            }
            .padding(start = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logout2),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
//                        tint = Color.Unspecified
        )
        Text(
            text = "Đăng xuất",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 16.sp
        )

    }
}

@Preview (backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewLogout() {
    Spacer(Modifier.fillMaxSize())
    LogoutProfileSection(
        onLogout = {},
    )
}