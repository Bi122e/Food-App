package com.example.foodapp.ui.screen.main.home.section

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodapp.presentation.extensions.pulseSkeleton
import com.example.foodapp.ui.theme.Gray100


@Composable
fun LoadingHomeTab(
 ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray100)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {

//                    Box(
//                        modifier = Modifier
//                            .size(120.dp)
//                            .clip(CircleShape)
////                            .pulseSkeleton(
////                            )
////                            .pulseSkeleton(
////                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
////                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
////
////                                )
//                            .pulseSkeleton(
//                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
//                                )
//                    )



                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Spacer(Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(30.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                )
                        )
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(30.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }



            Box(
                modifier = Modifier.background(
                    Color.White, RoundedCornerShape(
                        30.dp
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(15.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                )
                        )

                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(10.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                )
                        )


                    }

                    //time
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(25.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                )
                        )

                    }

                    //address
                    Column() {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(20.dp)
                                        .clip(CircleShape)
                                        .pulseSkeleton(
                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                )
                                //line
                                Box(
                                    Modifier.height(70.dp)

                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(40.dp)
                                    .clip(CircleShape)
//                                    .pulseSkeleton(
//                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
//                                    )
                                    .pulseSkeleton(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier.background(
                    Color.White, RoundedCornerShape(
                        30.dp
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(15.dp)
                                .clip(CircleShape)
//                                .pulseSkeleton(
//                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
//                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
//                                )
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                        )

                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(10.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                )
                        )


                    }

                    //time
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(25.dp)
                                .clip(CircleShape)
//                                .pulseSkeleton(
//                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
//                                )
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                        )

                    }

                    //address
                    Column() {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(150.dp)
                                        .height(20.dp)
                                        .clip(CircleShape)
                                        .pulseSkeleton(
                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.25f),
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                                        )
                                )
                                //line
                                Box(
                                    Modifier.height(70.dp)

                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(40.dp)
                                    .clip(CircleShape)
                                    .pulseSkeleton(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {

//                    Box(
//                        modifier = Modifier
//                            .size(120.dp)
//                            .clip(CircleShape)
////                            .pulseSkeleton(
////                            )
////                            .pulseSkeleton(
////                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
////                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
////
////                                )
//                            .pulseSkeleton(
//                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
//                                )
//                    )



                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        Spacer(Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .height(30.dp)
                                .clip(CircleShape)
                                .pulseSkeleton(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                                )
                        )
//                        Box(
//                            modifier = Modifier
//                                .width(200.dp)
//                                .height(30.dp)
//                                .clip(CircleShape)
//                                .pulseSkeleton(
//                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
//                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
//                                )
//                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .clip(CircleShape)
                            .pulseSkeleton(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }




@Preview
@Composable
fun PreviewLoadingHome() {
    LoadingHomeTab(
     )
}