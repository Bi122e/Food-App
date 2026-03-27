package com.example.foodapp.ui.screen.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.domain.model.Restaurant
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun RestaurantInfoSection(restaurant: Restaurant) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//        Text(text = restaurant.restaurantName, style = MaterialTheme.typography.titleMedium)

        //restaurant name
        Text(
            text = restaurant.restaurantName,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {


            //review
            Text(
                text = "${restaurant.rating} ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            Icon(
                imageVector = Icons.Default.Star,
                modifier = Modifier.size(18.dp),
                tint = Yellow0,
                contentDescription = null,

            )



            Text(
                text = " (${restaurant.totalReview}+)",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))

            //danh gia khac
            Text(
                text = "Đánh giá khác",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(
                        color = Yellow0,
                        shape = CircleShape
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }

}
