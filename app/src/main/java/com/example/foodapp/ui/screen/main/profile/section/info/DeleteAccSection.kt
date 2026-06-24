package com.example.foodapp.ui.screen.main.profile.section.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeleteAccSection() {

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(
                color = Color.White,
             ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Xóa tài khoản",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview (backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewDete() {
    Box(modifier = Modifier.fillMaxSize().background(Color.White))
    DeleteAccSection()
}