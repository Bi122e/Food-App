package com.example.foodapp.ui.screen.shared

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Gray65

@Composable
fun HomeTopBar(
    collapsed: Boolean,
    onNavigationToSearchTab: () -> Unit,
    badgeCount: Int,
    onNavigationToNotification: () -> Unit
) {

    // rút ngắn thời gian xuống 150ms và dùng LinearEasing để phản hồi ngay lập tức khi vuốt
    val animationSpecFloat = tween<Float>(durationMillis = 150, easing = LinearEasing)
    val animationSpecDp = tween<androidx.compose.ui.unit.Dp>(durationMillis = 150, easing = LinearEasing)
    val animationSpecIntSize = tween<IntSize>(durationMillis = 150, easing = LinearEasing)

     val addressHeight by animateDpAsState(
        targetValue = if (collapsed) 0.dp else 72.dp,
        animationSpec = animationSpecDp,
        label = "addressHeight"
    )

    // dộ mờ Alpha
    val addressAlpha by animateFloatAsState(
        targetValue = if (collapsed) 0f else 1f,
        animationSpec = animationSpecFloat,
        label = "addressAlpha"
    )

    val searchHeight by animateDpAsState(
        targetValue = if (collapsed) 48.dp else 60.dp,
        animationSpec = animationSpecDp,
        label = "searchHeight"
    )

     val horizontalPadding by animateDpAsState(
        targetValue = if (collapsed) 10.dp else 16.dp,
        animationSpec = animationSpecDp,
        label = "horizontalPadding"
    )

    val verticalPadding by animateDpAsState(
        targetValue = if (collapsed) 8.dp else 16.dp,
        animationSpec = animationSpecDp,
        label = "verticalPadding"
    )

    Box {
        // background
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = if (collapsed) {
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFFa8e1e8),
                                Color(0xFFb7e3ec),
                                Color(0xFFe9f6f9),
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            listOf(Color.White, Color.White)
                        )
                    }
                )
        )

        Column(modifier = Modifier.fillMaxWidth()) {

            // address
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(addressHeight)
                    .graphicsLayer { alpha = addressAlpha }
                    .clipToBounds()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFa8e1e8),
                                Color(0xFFb7e3ec),
                                Color(0xFFe9f6f9),
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Blue0
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        text = "Trường đại học ABC",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold
                    )

                    Box(
                        contentAlignment = Alignment.TopEnd,
                        modifier = Modifier.clickable(onClick = onNavigationToNotification)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = null,
                            tint = Color(0xFFf3b153),
                            modifier = Modifier.size(32.dp)
                        )

                        if (badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color(0xFFf76363), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeCount.toString(),
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // SEARCH BAR & MINI ICONS SECTION
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
             ) {

                //   location nhỏ
                AnimatedVisibility(
                    visible = collapsed,
                    enter = fadeIn(animationSpecFloat) + expandHorizontally(animationSpecIntSize),
                    exit = fadeOut(animationSpecFloat) + shrinkHorizontally(animationSpecIntSize),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = Blue0,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(28.dp)
                    )
                }

                // search
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = verticalPadding
                        )
                        .height(searchHeight)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clip(RoundedCornerShape(30.dp))
                        .border(
                            BorderStroke(0.3.dp, Gray65),
                            RoundedCornerShape(30.dp)
                        )
                        .background(Color.White)
                        .clickable { onNavigationToSearchTab() }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = Color.Black.copy(0.6f)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Bạn muốn ăn gì hôm nay?",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black.copy(0.5f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                //notification nhỏ
                AnimatedVisibility(
                    visible = collapsed,
                    enter = fadeIn(animationSpecFloat) + expandHorizontally(animationSpecIntSize),
                    exit = fadeOut(animationSpecFloat) + shrinkHorizontally(animationSpecIntSize),
                ) {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable(onClick = onNavigationToNotification)
                        ,
                        contentAlignment = Alignment.TopEnd
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = null,
                            tint = Color(0xFFf3b153),
                            modifier = Modifier.size(28.dp)
                        )

                        if (badgeCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(Color(0xFFf76363), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = badgeCount.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewScreen() {
    HomeTopBar(
        collapsed = false,
        onNavigationToSearchTab = {},
        badgeCount = 1,
        onNavigationToNotification = {}
    )
}