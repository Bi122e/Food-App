package com.example.foodapp.ui.screen.main.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import com.example.foodapp.R
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray85
import kotlin.math.sin

@Composable
fun ChatTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val tabItems = listOf("Liên hệ", "Mọi người")
        var selected by remember { mutableStateOf(1) }

        Scaffold(
            topBar = {
                TopChatTab()
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

                    SearchChatSelection()
                }

                item {

                    when (selected) {

                        0 -> {

                            if (false) {
                                //trong
                            } else {
                                //lam gi do
                            }
                        }

                        1 -> {

                            if (true) {
                                ConversationsChatSelection()
                            } else {
                                //ko co du lieu
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationsChatSelection() {

    val listConversations = listOf(1,2,3,4,5)

    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        listConversations.forEachIndexed { index, item ->

            Row() { }

        }
    }
}
@Composable
fun SearchChatSelection() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Gray100,
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
            tabItems.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(6.dp)
                        .background(
                            color = if (selected == index)
                                Color.Black
                            else
                                Color.Transparent,
                            RoundedCornerShape(30.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .clickable {
                            onSelectedChange(index)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = if (selected == index)
                            Color.White
                        else
                            Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopChatTab() {

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

@Preview
@Composable
fun ChatTabPreview() {
    ChatTab()
}
