package com.example.foodapp.ui.screen.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
   import com.example.foodapp.ui.theme.Blue4
import kotlinx.coroutines.delay

@Composable
fun SnackBar(
    onTurnOffSnackBar: () -> Unit,
    showMessage: Boolean,
    message: String,
) {

    LaunchedEffect(showMessage) {
        if (showMessage) {
            delay(2000)
            onTurnOffSnackBar()
        }
    }
         AnimatedVisibility(
            visible = showMessage,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it }
        ) {

            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp, vertical = 16.dp)
                    .clickable(
                        onClick = { onTurnOffSnackBar() }
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Blue4,
                    contentColor = Color.White
                )
            ) {
                Row (
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        color = Color.White,
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
fun PreviewSnackBar() {
    Box(Modifier
        .fillMaxSize()
        .background(Color.White))
    SnackBar(
        onTurnOffSnackBar = {},
        showMessage = true,
        message = "cap nhat thanh cong"
     )
}