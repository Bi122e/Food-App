package com.example.foodapp.ui.screen.main.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.domain.model.Restaurant

@Composable
fun RestaurantHeaderSection(restaurant: Restaurant, onClickBackHome: () -> Unit) {
    Column {
        Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            // Cover Image
            AsyncImage(
                model = restaurant.coverImage, //
                contentDescription = null,
                placeholder = painterResource(R.drawable.bg_box2),
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentScale = ContentScale.Crop
            )

            //Back/Share/Favorite
            HeaderRestaurantUI(onClickBackHome = onClickBackHome)

            // Avatar
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(70.dp)
                    .align(Alignment.BottomStart)
                    .shadow(4.dp, RoundedCornerShape(20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = restaurant.imageUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_loading),
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp))
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Info
        RestaurantInfoSection(restaurant)
    }
}