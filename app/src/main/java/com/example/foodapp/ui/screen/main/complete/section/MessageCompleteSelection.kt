package com.example.foodapp.ui.screen.main.complete.section

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.theme.Blue1

@Composable
fun MessageCompleteSelection(
    uiState: CompleteUiState,
    onChangedMessage: (String) -> Unit
) {
    val textPreview = uiState.message
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = textPreview,
        onValueChange = {
            if (it.length <= 150) {
                onChangedMessage(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 100.dp, max = 140.dp)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        maxLines = 150,
        textStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold
        ),
        decorationBox = { innerTextField ->

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 140.dp)
                    .border(
                        width = 1.dp,
                        color = if (isFocused) Blue1 else Color.LightGray,
                        shape = RoundedCornerShape(15.dp)
                    )
            ) {

                if (textPreview.isEmpty()) {
                    Text(
                        text = "Nhập lời khen giành cho quán.",
                        fontSize = 14.sp,
                        color = Color.Black.copy(alpha = 0.4f),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 28.dp
                        )
                ) {
                    innerTextField()
                }

                Text(
                    text = "${textPreview.length}/150 ký tự",
                    fontSize = 12.sp,
                    color = Color.Black.copy(alpha = 0.2f),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 12.dp,
                            bottom = 8.dp
                        )
                )
            }
        }
    )
}