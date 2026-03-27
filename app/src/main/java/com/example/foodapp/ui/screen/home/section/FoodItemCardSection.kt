package com.example.foodapp.ui.screen.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.utils.toVND
import com.example.foodapp.domain.model.Food
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun FoodItemCard(item: Food, onClickFavorite: (foodId: String) -> Unit, isFavorite: Boolean) {
    val isPreview = LocalInspectionMode.current
    Box(
        modifier = Modifier
            .shadow(1.dp, RoundedCornerShape(24.dp), clip = true)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
    ) {
        Column {
            // img food
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                AsyncImage(
                    model = item.imgUrl,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.bg_box1),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // favorite
                Icon(
                    imageVector = if (isPreview) {
                        Icons.Default.FavoriteBorder
                    } else {
                        if (isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        }
                    },
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier
                        .padding(10.dp).align(Alignment.TopEnd).size(25.dp)
                        .background(Color(0xFFf2eded), CircleShape).padding(3.dp)
                        .clickable { onClickFavorite(item.foodId)}
                )
            }

            // info food
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${item.averageRating} ",
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Yellow0
                    )
                    Text(
                        text = " (${item.reviewCount}+)",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.price.toVND(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Black, RoundedCornerShape(30.dp))
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF34c2c3), RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}