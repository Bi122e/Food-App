package com.example.foodapp.ui.screen.main.profile.section.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun TopBarInfoSelection(
    onNavigationToBack: () -> Unit,
) {


        Column(
            modifier = Modifier
                .background(Color.White),
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cập nhật tài khoản",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {

                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp).clickable(onClick = onNavigationToBack)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.padding(horizontal = 16.dp)


            )

            Spacer(Modifier.height(10.dp))

            Spacer(
                Modifier
                    .background(Color.LightGray.copy(0.2f))
                    .height(4.dp)
                    .fillMaxWidth()
            )
        }
    }
