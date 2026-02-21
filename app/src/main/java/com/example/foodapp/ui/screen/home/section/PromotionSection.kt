package com.example.foodapp.ui.screen.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.domain.model.Promotion
import kotlinx.coroutines.delay

@Composable
fun PromotionSection(

    promotionState: UiState<List<Promotion>>,

    ) {

    when (promotionState) {

        is UiState.Success -> {

            val promotions = promotionState.data

            if (promotionState.isEmpty()) return

            val pagerState = rememberPagerState(
                initialPage = Int.MAX_VALUE / 2,
                pageCount = { Int.MAX_VALUE }
            )

            val realPage = pagerState.currentPage % promotions.size

            LaunchedEffect(pagerState) {
                while (true) {
                    delay(3000)
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp)),

                    ) {

                    HorizontalPager(
                        state = pagerState
                    ) { page ->

                        val index = page % promotions.size

                        AsyncImage(
                            model = promotions[index].promoUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                //dot
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(promotions.size) { index ->

                        val isSelected = index == realPage

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(if (isSelected) 20.dp else 8.dp)
                                .clip(RoundedCornerShape(40.dp))
                                .height(8.dp)
                                .background(if (isSelected) Color.Black else Color.Gray)

                        )
                    }
                }
            }
        }

        else -> {
            showToast(context = LocalContext.current, "KHONG TIM THAT")
        }
    }
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .height(160.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        HorizontalPager(
//            state = promotionState
//        ) { page ->
//
//            AsyncImage(
//                model = promotionState[page],
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
}