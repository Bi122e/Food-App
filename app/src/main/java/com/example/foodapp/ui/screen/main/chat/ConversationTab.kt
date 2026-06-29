package com.example.foodapp.ui.screen.main.chat

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.core.utils.toChatTime
import com.example.foodapp.domain.model.Conversation
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Gray65

@Composable
fun ConversationTab(
    conversations: List<Conversation>,
    onNavigationToBack: () -> Unit,
    onNavigationToMessage: (String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {



        val tabItems = listOf("Liên hệ", "Mọi người")
        var selected by remember { mutableStateOf(1) }


        Scaffold(
            topBar = {
                TopChatTab(
                    onNavigationToBack = onNavigationToBack
                )
            },
            containerColor = Color.White
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {


                item {
                    HeaderChatSelection(
                        selected = selected,
                        tabItems = tabItems,
                        onSelectedChange = {
                            selected = it
                        }
                    )
                }




                item {

                    SearchChatSelection(
                    )
                }

                item {
                    Spacer(Modifier.height(10.dp))
                }

                if (selected == 0) {
                    item {
                        EmptyMessageSelection("Hiện chưa cập nhật tính năng này")
                    }
                }


                if (selected == 1) {
                    if (conversations.isNotEmpty()) {
                        items(conversations) { item ->
                            ConversationsChatSelection(item, onNavigationToMessage)
                        }
                    } else {
                        item {
                            EmptyMessageSelection()
                        }
                    }
                }


                item {
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyMessageSelection(
    text: String = "Hiện không có tin nhắn nào"
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.bg_empty_message1),
                contentDescription = null
            )
        }

        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = Gray65
        )
    }
}

@Composable
fun ConversationsChatSelection(
    conversation: Conversation,
    onNavigationToMessage: (String) -> Unit
) {


    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.clickable(onClick = { onNavigationToMessage(conversation.conversationId) })
    ) {

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    Blue2.copy(0.5f),
                    CircleShape
                )
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = null,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Blue0.copy(0.7f)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_avatar2),
                placeholder = painterResource(R.drawable.ic_avatar2)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {

            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = conversation.displayName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = conversation.lastMessageTime?.toChatTime() ?: "---",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = 4.dp),
                    color = Gray65
                )
            }
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text =
                        if (conversation.driverId == conversation.lastMessageSenderId)
                            "tài xế: ${conversation.lastMessage}"
                        else
                            "Bạn: ${conversation.lastMessage}",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(2f),
                    color = if (conversation.unreadCount > 0)
                        Color.Black
                    else
                        Gray65,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.weight(0.25f))

                if (conversation.unreadCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(
                                Blue0,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            color = Color.White
                        )
                    }
                }
            }


        }

    }
    Spacer(Modifier.height(5.dp))
}


@Composable
fun SearchChatSelection() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFf8f8f8),
                RoundedCornerShape(10.dp)
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search1),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp),
                colorFilter = ColorFilter.tint(Color.Black.copy(0.3f))
            )

            Text(
                text = "Tìm kiếm",
                fontWeight = FontWeight.Medium,
                color = Color.Black.copy(0.3f)
            )
        }


    }
}

@Composable
fun HeaderChatSelection(
    selected: Int,
    tabItems: List<String>,
    onSelectedChange: (Int) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .coloredShadow(
                colors = listOf(Color.Black),
                alpha = 0.1f,
                borderRadius = 30.dp,
                blurRadius = 5.dp
            )
            .background(
                Color.White,
                RoundedCornerShape(30.dp)
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            SlidingTabRow(
                tabItems = tabItems,
                selected = selected,
                onSelectedChange = { onSelectedChange(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopChatTab(
    onNavigationToBack: () -> Unit,
) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Trò chuyện",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        },
        navigationIcon = {

            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable(onClick = onNavigationToBack)
                    .background(
                        Color.Black.copy(0.1f),
                        CircleShape
                    )
                    .padding(6.dp)

            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SlidingTabRow(
    tabItems: List<String>,
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                Color(0xFFF2F2F2),
                RoundedCornerShape(30.dp)
            )
            .padding(4.dp)
    ) {

        val tabWidth = maxWidth / tabItems.size

        val animatedOffset by animateDpAsState(
            targetValue = tabWidth * selected,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            ),
            label = "indicator"
        )

        // Indicator trượt
        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .width(tabWidth)
                .fillMaxHeight()
                .background(
                    Color.Black,
                    RoundedCornerShape(30.dp)
                )
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabItems.forEachIndexed { index, item ->

                val textColor by animateColorAsState(
                    targetValue = if (selected == index)
                        Color.White
                    else
                        Color.Black,
                    label = "textColor"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable {
                            onSelectedChange(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatTabPreview() {
    ConversationTab(
        conversations =
            List(6) {
                Conversation(
                    displayName = "Nguyễn thị tú",
                    lastMessage = "xin chào quý khác, đơn hàng của bạn sẽ được giao trong thời gian sớm nhất",
                    unreadCount = 1
                )
            },

        onNavigationToBack = {},
        onNavigationToMessage = {},
    )
}
