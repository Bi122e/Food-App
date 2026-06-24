package com.example.foodapp.ui.screen.main.profile.section.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.ui.theme.Blue1

@Composable
fun UpdateInfoSelection(
    setClickedUpdate: () -> Unit,
    isSavedEnable: Boolean,

) {


    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 30.dp)
            .fillMaxWidth()
            .clickable(
                enabled = isSavedEnable,
                onClick = {
                    setClickedUpdate()
                }
            )
            .background(
                if (isSavedEnable) Blue1 else Blue1.copy(0.3f),
                RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Cập nhật",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier

                .padding(16.dp)
        )
    }
}


@Preview
@Composable
fun PreviewUpdateInfoSelection() {
    Box(Modifier.fillMaxSize().background(Color.White))
    UpdateInfoSelection(
        setClickedUpdate = {},
        isSavedEnable = false
    )
}