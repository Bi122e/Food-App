package com.example.foodapp.ui.screen.main.complete.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.foodapp.R
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Blue4


@Composable
fun CompleteNotificationSection(
    uiState: CompleteUiState,
    onNavigationToBack: () -> Unit,
) {


    Dialog(
        onDismissRequest = {
            onNavigationToBack()
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .padding(vertical = 30.dp, horizontal = 16.dp),
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {



                    val composition1 by rememberLottieComposition(
                        LottieCompositionSpec.Asset("success2.json")
                    )
                    val progress1 by animateLottieCompositionAsState(
                        composition1,
                        iterations = 1
                    )

                    val isCompleted = progress1 == 1f


                    Box(
                        contentAlignment = Alignment.TopCenter
                    ) {
                        LottieAnimation(
                            composition = composition1,
                            progress = progress1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        LottieAnimation(
                            composition = composition1,
                            progress = progress1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )


                        Image(
                            painter = painterResource(R.drawable.bg_balloon1),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                        )
                    }

                    Text(
                        text = "Cảm ơn bạn đã đánh giá",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("Với đánh giá của bạn")

                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(" \"${uiState.restaurantName}\" ")
                            }

                            append("sẽ ngày càng tốt hơn. Chúng tôi luôn mang đến trải nghiệm tốt nhất cho bạn.")
                        }
                    )

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onNavigationToBack)
                            .background(
                                color = Blue1,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(vertical = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "Xong",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }


            }
        }
    }

}


@Preview
@Composable
fun CompleteNotificationSectionPreview() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    CompleteNotificationSection(
        uiState = CompleteUiState(
            restaurantName = "Nước ép Trinh Trinh - Khởi Nghĩa"
        ),
        onNavigationToBack = {},
    )
}