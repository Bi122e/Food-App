package com.example.foodapp.ui.screen.initializationInfo

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogProgress(
    onCloseDialog: () -> Unit,
    onExitProgram: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = { onCloseDialog() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "Thoát ứng dụng",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )

                    Text(
                        text = "Bạn có muốn thoát ứng dụng không?",
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.Black.copy(0.6f)
                    )

                    Spacer(Modifier.height(25.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Không",
                            fontSize = 16.sp,
                            color = Blue1,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    onCloseDialog()
                                }
                                .padding(start = 12.dp)
                                .width(140.dp)
                                .border(
                                    BorderStroke(1.dp, Blue1),
                                    RoundedCornerShape(30.dp)
                                )
                                .background(Blue2.copy(0.4f), RoundedCornerShape(30.dp))
                                .padding(vertical = 14.dp)

                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = "Có",
                            fontSize = 16.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .clickable {
                                    onExitProgram()
                                }
                                .padding(end = 12.dp)
                                .width(140.dp)
                                .background(Blue1, RoundedCornerShape(30.dp))
                                .padding(vertical = 14.dp)
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFF)
@Composable
fun PreviewDialogProgress() {
    Box(Modifier.fillMaxSize())
    DialogProgress(
        onExitProgram = {},
        onCloseDialog = {},
    )
}