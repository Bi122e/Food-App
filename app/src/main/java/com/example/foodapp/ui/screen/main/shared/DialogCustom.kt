package com.example.foodapp.ui.screen.main.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
 import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DialogCustom (
    onDismiss: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White))
    Dialog(
        onDismissRequest = { onDismiss() }, //duoc goi khi bam ra ngoai, back
        properties = DialogProperties(
            usePlatformDefaultWidth = false //lay chieu rong toi da man hinh
        )
    ) {
        FavoriteDialog( //root container/ bg mau den
            onDismiss = onDismiss
        )
    }
}


@Composable
fun FavoriteDialog(
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        //bg behind
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
                .clickable(
                    onClick = {
                        onDismiss()
                    }
                )
        )

        FavoritePanel(
            modifier = Modifier.align(Alignment.Center),
            onDismiss = onDismiss,
        )
    }
}


@Composable
fun FavoritePanel(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
            .offset {
                IntOffset(
                    x = 0,
                    y = offsetY.toInt()
                )
            }
            .pointerInput(Unit) { //Cho phép bắt gesture
                detectVerticalDragGestures( //Lắng nghe thao tác kéo dọc
                    onVerticalDrag = {_, dragAmount -> //Kéo xuống => dương, Kéo lên => âm

                        if (dragAmount > 0) { //chỉ thay đổi trục y khi kéo xuống (âm)
                            offsetY += dragAmount
                        }
                    },
                    onDragEnd =  { //Được gọi khi thả tay

                        if (offsetY > 300f) { //keo du xa, đóng dialog
                            onDismiss()
                        } else {
                            offsetY = 0f //keo ko du quay lai vị trí cũ
                            //val offsetY by animateFloatAsState(...) option
                        }
                    }
                )
            }
            .background( //bg bo tron
                color = Color.White,
                shape = RoundedCornerShape(
                    20.dp
                )
            )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
         ) {

            Spacer(Modifier.height(12.dp))


            //drag handle
            Box( // -
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(48.dp)
                    .height(5.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(999.dp)
                    )
            )

            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

            }

        }
    }
}

@Preview
@Composable
fun PreviewDialog() {
    DialogCustom( onDismiss = {} )
}