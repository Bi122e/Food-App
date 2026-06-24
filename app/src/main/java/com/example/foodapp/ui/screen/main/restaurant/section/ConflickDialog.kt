package com.example.foodapp.ui.screen.main.restaurant.section

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.foodapp.ui.theme.Blue1

@Composable
fun ConflictDialog(
    showDialog: Boolean,
    onDialogToClose: () -> Unit,
    onForceAddItem:  () -> Unit,
    title: String,
    message: String,
) {

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDialogToClose() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false

            )
        ) {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .shadow(elevation = 1.dp, RoundedCornerShape(20.dp))
                        .background(Color.White, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = message,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black.copy(0.5f)
                        )

                        Spacer(Modifier.height(20.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)

                        ) {

                            //Giỏ hàng hiện tại thuộc nhà hàng khác, bạn có muốn xóa nhà hàng hiện tại k
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Blue1, RoundedCornerShape(15.dp))
                                    .background(Blue1.copy(0.2f))
                                    .width(150.dp)
                                    .height(40.dp)
                                    .clickable(onClick = onForceAddItem),
                                contentAlignment = Alignment.Center
                                ) {
                                Text(
                                    text = "Có",
                                    color = Blue1
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Box(
                                modifier = Modifier
                                    .border(1.dp, Blue1, RoundedCornerShape(20.dp))
                                    .background(Blue1, RoundedCornerShape(15.dp))
                                    .width(150.dp)
                                    .height(40.dp)
                                    .clickable{
                                        onDialogToClose()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Không",
                                    color = Color.White
                                )
                            }
                        }

                    }
                }
            }

        }
    }


@Preview
@Composable
fun PreviewConflictDialog() {

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    ConflictDialog(
        showDialog = true,
        onDialogToClose = {},
        title = "Xósda",
        message = "helsdfsdsdfsdfsdflo",
        onForceAddItem = {},
    )
}