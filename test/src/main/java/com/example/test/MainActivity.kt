package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


        }
    }
}


data class NavBottomItem(
    val title: String, // Sửa lỗi chính tả tittle -> title
    val route: String,
    val selected: ImageVector,
    val unSelected: ImageVector
)
