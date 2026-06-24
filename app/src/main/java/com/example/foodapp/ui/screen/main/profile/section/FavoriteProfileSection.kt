package com.example.foodapp.ui.screen.main.profile.section
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
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
fun FavoriteProfileSection(
    onClickFavorite: () -> Unit,
    modifier: Modifier,
) {

    Box(
        modifier = modifier
            .size(90.dp)
            .clickable(
                onClick = {
                    onClickFavorite()
                }
            )
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
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .padding(6.dp)
        ) {

            Icon(
                imageVector = Icons.Rounded.Favorite,
                contentDescription = null,
                tint = Color.Red.copy(0.4f),
                modifier = Modifier
                    .size(32.dp)
            )

            Text(
                text = "Yêu thích",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(0.7f)
            )
        }
    }
}