package com.example.foodapp.ui.screen.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodapp.R
import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Category


@Composable
fun CategorySelection(categoryState: UiState<List<Category>>) {

    when (categoryState) {
        is UiState.Success -> {

            val categories = categoryState.data
//            Row (
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clip(RoundedCornerShape(40.dp))
//                            .background(Color.Transparent)
//                    ) {
//                        AsyncImage(
//                            model = categories.forEach { it.iconUrl }
//                        )
//                    }
//                }
//            }
            var selectedCategory by remember { mutableStateOf<String?>(null) }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(categories) { category ->

                    val isSelected = selectedCategory == category.name

                    Column(
                        modifier = Modifier
                            .width(90.dp)
                            .clickable{
                                selectedCategory = category.name
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ){

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(25.dp))
                                .size(65.dp)
                                .background(
                                    if (isSelected) {
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xffFFE0B2),
                                                Color(0xffFF8C00)
                                            )
                                        )
                                    } else {
                                        Brush.radialGradient(
                                            colors = listOf(
                                                Color.White,
                                                Color(0xffFAD5A5)
                                            )
                                        )
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(30.dp),
                                   model = category.iconUrl,
                                contentDescription = null,
                                placeholder = painterResource(R.drawable.ic_loading),
                            )
                        }

                        Spacer(Modifier.height(7.dp))
                        Text(
                            text = category.name,
                            color = Color.Black,
                            fontSize = 14.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        }

        is UiState.Loading -> {
            LazyRow {
                items(5) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }

        else -> {}
    }
}