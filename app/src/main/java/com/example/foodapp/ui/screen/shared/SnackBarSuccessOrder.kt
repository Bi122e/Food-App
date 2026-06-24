package com.example.foodapp.ui.screen.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue4
import kotlinx.coroutines.delay


@Composable
fun SnackBarSuccessOrder(
    showSnackBar: Boolean,
    onValueChange: () -> Unit,
) {


    LaunchedEffect(showSnackBar) {
        if (showSnackBar) {
            delay(5000)
            onValueChange()

        }
    }

    AnimatedVisibility(
        visible = showSnackBar,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp)
                .clickable(
                    onClick = {}
                )
                .background(
                    Blue4,
                    RoundedCornerShape(10.dp)
                )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            ) {

                Text(
                    text = "Giao hàng thành công - đánh giá ngay",
                    color = Color.White
                )

                Spacer(Modifier.weight(1f))

                Icon(
                    Icons.Rounded.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }


}


@Preview
@Composable
fun SnackBarSuccessOrderPreview() {

    Box(Modifier
        .fillMaxSize()
        .background(Color.White))
    SnackBarSuccessOrder(
        showSnackBar = true,
        onValueChange = {},
    )
}