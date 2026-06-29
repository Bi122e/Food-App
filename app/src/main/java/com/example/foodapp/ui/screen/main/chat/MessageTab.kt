package com.example.foodapp.ui.screen.main.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Gif
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.core.utils.toChatDate
import com.example.foodapp.core.utils.toChatTime
import com.example.foodapp.domain.model.Message
import com.example.foodapp.presentation.state.ChatUiState
import com.example.foodapp.presentation.state.MessageUi
import com.example.foodapp.ui.screen.main.food.topShadow
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray85
import com.example.foodapp.ui.theme.White
import isSameDay
import kotlinx.coroutines.launch
import tiledBackground
import java.sql.Date


@Composable
fun MessageTab(
    chatUiState: ChatUiState,
    onSendMessage: () -> Unit,
    onTextChanged: (String) -> Unit,
    onNavigationToBack: () -> Unit,
 ) {


    val listState = rememberLazyListState()
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBarMessageSelection(
                onNavigationToBack = onNavigationToBack
            )
        },
        bottomBar = {
            BottomBarMessageSelection(
                onSendMessage = onSendMessage,
                onTextChanged = onTextChanged,
                chatUiState = chatUiState,
                listState = listState,
             )
        },
        containerColor = Color.White
    ) { paddingValues ->

        val pattern = ImageBitmap
            .imageResource(R.drawable.bg_whatapps3)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .tiledBackground(pattern)
        )
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 50.dp,
                bottom = paddingValues.calculateBottomPadding() + 20.dp
            ),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            item {
                if (chatUiState.isDelivered) {
                    MessageDisable(
                        listState = listState
                    )
                }
            }
            itemsIndexed(

                chatUiState.messages,
                key = { index, item ->
                    item.message.messageId
                }
            ) { index, item ->

                 val olderMessage = chatUiState.messages.getOrNull(index + 1)
                val showDate = olderMessage == null ||
                        !isSameDay(
                            item.message.createdAt,
                            olderMessage.message.createdAt
                        )
                ContentMessageSelection(
                    modifier = Modifier.animateItem(),
                    showDate = showDate,
                    text = item.message.text,
                    isMine = item.isMine,
                    time = item.message.createdAt?.toChatTime() ?: "----",
                    date = item.message.createdAt?.toChatDate() ?: "----",
                )
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MessageDisable(
    listState: LazyListState
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 30.dp, horizontal = 16.dp)
                .background(
                    Color.White,
                    RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = "Đơn hàng đã hoàn thành, đoạn chat sẽ dừng hoạt động và sẽ được xóa sau 15 ngày.",
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }

    }
    scope.launch {
        listState.animateScrollToItem(0)
    }

}

@Composable
fun ContentMessageSelection(
    modifier: Modifier,
    text: String,
    isMine: Boolean,
    time: String,
    date: String,
    showDate: Boolean,
) {

    val width = LocalConfiguration.current
    val screenWidth = width.screenWidthDp.dp
    var placeTimeOnLastLine by remember { mutableStateOf(true) }
    var timeWidthPx by remember { mutableIntStateOf(0) }

    val bubbleColor = if (isMine) {
        Color(0xFFd0fecf)
    } else {
        White
    }
    val textColor = if (isMine) {
//        Color(0xFF0a0a0a)
        Color.Black
    } else
        Color.Black


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showDate) {
            Box(
                modifier = Modifier
                    .background(Color.LightGray, CircleShape)
                    .padding(vertical = 6.dp, horizontal = 9.dp)
            ) {
                Text(
                    text = date,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        //tại sao box này ko nằm trên if ở dưới

        if (isMine) {

            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd,

                ) {


                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {


                    Box(
                        modifier = Modifier
                            .background(
                                bubbleColor,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(9.dp),
                    ) {

                        Text(
                            text = text,
                            color = textColor,

                            )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                    ) {

                        Text(
                            text = time,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_double_check2),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            colorFilter = ColorFilter.tint(Blue0)
                        )

                    }


                }

            }

        } else {

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {

                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),

                        ) {

                        //avatar
                        Box(
                            modifier = Modifier
                                .size(25.dp)
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

                        //container text
                        Box(
                            modifier = Modifier
                                .background(
                                    color = bubbleColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(9.dp)
                        ) {
                            Text(
                                text = text,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)


                            )

                        }

                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .padding(start = 0.dp, top = 0.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_double_check2),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            colorFilter = ColorFilter.tint(Color.Transparent)
                        )

                        Text(
                            text = time,
                            modifier = Modifier.padding(end = 5.dp),
                            fontSize = 10.sp,
                            color = Color.Gray
                        )

                    }
                }

            }

        }
    }


}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomBarMessageSelection(
    onSendMessage: () -> Unit,
    onTextChanged: (String) -> Unit,
    chatUiState: ChatUiState,
    listState: LazyListState,
 ) {

    val scope = rememberCoroutineScope() //tao scope de goi trong callback
    val imeVisible = WindowInsets.isImeVisible
//    val imeVisible = true
    val keyboardController = LocalSoftwareKeyboardController.current//tắt bàn phím
    val focusManager = LocalFocusManager.current//tắt focus
    //or
//    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
//    val imeVisibility = imeBottom > 0

    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        color = Color(0xFFf5f2ea),
        modifier = Modifier
            .topShadow(10.dp, Color(0xFFf55d5d))
            .imePadding()
            .offset(
                y = if (imeVisible)
                    (-20).dp
                else
                    0.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .padding(bottom = 6.dp)

        ) {

            OutlinedTextField(
                enabled = !chatUiState.isDelivered,
                value = chatUiState.text,
                onValueChange = {
                    onTextChanged(it)
                },
                placeholder = {
                    Text(
                        text = "Nhập tin nhắn.."
                    )
                },
                maxLines = 4,
                shape = RoundedCornerShape(30.dp),
                trailingIcon = {
                    if (!imeVisible) {
                        Icon(
                            Icons.Rounded.Gif,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 14.dp)
                                .coloredShadow(
                                    colors = listOf(Color.Black),
                                    alpha = 0.1f,
                                    blurRadius = 3.dp
                                )
                                .size(38.dp)
                                .background(
                                    color =  if(!chatUiState.isDelivered)
                                        Color.White
                                    else
                                        Gray100.copy(0.5f),
                                    CircleShape,
                                )
//                            .padding(3.dp)
                        )
                    }
                },
                modifier = Modifier.weight(0.6f),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    focusedBorderColor = Color(0xFF807d7d),
                    unfocusedBorderColor = Color.Transparent,
                    disabledContainerColor = Gray85
                )

            )

            if (!imeVisible) {
                Icon(
                    Icons.Rounded.Mic,
                    contentDescription = null
                )
            }

            if (!imeVisible) {
                Icon(
                    Icons.Rounded.CameraAlt,
                    contentDescription = null
                )
            }



            if (imeVisible) {
                Icon(
                    Icons.Rounded.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .clickable(
                            enabled = !chatUiState.isDelivered || chatUiState.text.isNotEmpty(),
                            onClick =
                                {
                                    scope.launch {
                                        listState.animateScrollToItem(0)
                                    }
                                    onSendMessage()
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                        )
                        .background(
                            color =   if (chatUiState.text.isNotEmpty())
                                Color(0xFF1dab61)
                            else
                                Color(0xFF1dab61).copy(0.4f),
                            CircleShape
                        )
                        .padding(12.dp)
                )
            }

        }
    }
}

@Composable
fun TopBarMessageSelection(
    onNavigationToBack: () -> Unit,
) {

    Box(
        modifier = Modifier
            .shadow(
                elevation = 5.dp,
                spotColor = Color.Red
            )
            .background(Color(0xFFf5f2ea))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onNavigationToBack),
                tint = Color.Black
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                modifier = Modifier.weight(1f)
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
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

                Text(
                    text = "Nguyễn Trọng Bình",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Image(
                painter = painterResource(R.drawable.ic_video_call1),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )


            Image(
                painter = painterResource(R.drawable.ic_phone2),
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
        }
    }

}

@Preview
@Composable
fun MessageTabPreview() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    MessageTab(
        chatUiState = ChatUiState(
            messages =
                listOf(
                    MessageUi(
                        message = Message(
                            messageId = "asdasd",
                            text = "T9sdf sdfsdfsdfsdfsdfsdfsdfsdf",
                            createdAt = Date(System.currentTimeMillis())
                        ),
                        isMine = false
                    ),
                    MessageUi(
                        message = Message(
                            text = "T9sdfsdfsdfsdfsdfsdfsdfsdfsdfT9s fsdfs ",
                            createdAt = Date(System.currentTimeMillis())
                        ),
                        isMine = true
                    ),
                    MessageUi(
                        message = Message(
                            messageId = "asdadfsd",
                            text = "T9sdf sdfsdfsdfsdfsdfsdfsdfsdf",
                            createdAt = Date(System.currentTimeMillis())
                        ),
                        isMine = false
                    ),
                )
        ),
        onSendMessage = {},
        onNavigationToBack = {},
        onTextChanged = {},
     )
}