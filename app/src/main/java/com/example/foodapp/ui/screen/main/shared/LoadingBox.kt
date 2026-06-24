package com.example.foodapp.ui.screen.main.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodapp.presentation.extensions.pulseSkeleton


@Composable
fun LoadingBox(
    rounded: Int = 20,
    modifier: Modifier,
    color: Color = Color.LightGray,
    lightColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
    darkColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)

) {

    Box(
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(rounded)
            )
            .pulseSkeleton(
                lightColor,
                darkColor,
            )
    )
}


@Preview
@Composable
fun PreviewLoadingBox() {
    LoadingBox(
        modifier = Modifier.size(70.dp)
    )
}