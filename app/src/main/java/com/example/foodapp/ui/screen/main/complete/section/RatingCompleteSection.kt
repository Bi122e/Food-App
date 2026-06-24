package com.example.foodapp.ui.screen.main.complete.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SentimentDissatisfied
import androidx.compose.material.icons.rounded.SentimentNeutral
import androidx.compose.material.icons.rounded.SentimentSatisfied
import androidx.compose.material.icons.rounded.SentimentVeryDissatisfied
import androidx.compose.material.icons.rounded.SentimentVerySatisfied
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.theme.Yellow0

@Composable
fun RatingCompleteSection(
    uiState: CompleteUiState,
    onChangedRating: (Int) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Cảm nhận của bạn về đơn đặt này?",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(0.5f)
        )


        val rattingItems = listOf(
            Triple(Icons.Rounded.SentimentVeryDissatisfied, "Sao mà đỡ được!", Yellow0),
            Triple(Icons.Rounded.SentimentDissatisfied, "Không giòn đâu!", Yellow0),
            Triple(Icons.Rounded.SentimentNeutral, "Ổn áp!", Yellow0),
            Triple(Icons.Rounded.SentimentSatisfied, "Mlem mlem!", Yellow0),
            Triple(Icons.Rounded.SentimentVerySatisfied, "Tuyệt đối điện ảnh!", Yellow0),
        )
        val rating = uiState.rating ?: -1

        val selectedItem = rattingItems.getOrNull(rating - 1)
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            repeat(5) { index ->

                IconButton(
                    onClick = {
                        onChangedRating(index + 1)
                    },
                    modifier = Modifier
//                        .fillMaxWidth(0.10f)
//                        .aspectRatio(1f)
                        .size(50.dp)
                ) {
                    Icon(
                        imageVector = if (rating > index)
                            Icons.Rounded.Star
                        else
                            Icons.Rounded.StarOutline,
                        contentDescription = null,
                        tint = if (rating > index)
                            Yellow0
                        else
                            Color.Black.copy(0.7f),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }


        selectedItem?.let { (image, text, color) ->

            Icon(
                imageVector = image,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = color
            )

            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}