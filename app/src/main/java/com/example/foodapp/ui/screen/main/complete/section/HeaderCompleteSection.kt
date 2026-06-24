package com.example.foodapp.ui.screen.main.complete.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.theme.Gray100

@Composable
fun HeaderCompleteSection(
    uiState: CompleteUiState
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //avatar
        Box(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Gray100
                )

        ) {
            AsyncImage(
                model = uiState.restaurantImgUrls,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                placeholder = painterResource(R.drawable.icon_delivery1),
                error = painterResource(R.drawable.icon_delivery1),
                contentScale = ContentScale.Crop,
            )
        }

        Text(
            text = uiState.restaurantName,
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        )
    }
}