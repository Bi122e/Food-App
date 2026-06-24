package com.example.foodapp.ui.screen.main.profile.section.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray65
import kotlin.text.isNullOrEmpty

@Composable
fun EmailInfoSelection(
    uiState: ProfileUiState,
    onEmailChange: (String) -> Unit,
    hasError: Map<String, String?>,
    isClickUpdate: Boolean,
    resetClickedUpdate: () -> Unit,
) {
    var onFocusChange by remember { mutableStateOf(false) }
    LaunchedEffect(isClickUpdate) {
        if (isClickUpdate) {
            onFocusChange = false
        }
    }
    Column() {
        Text(
            text = "Email",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(15.dp))


        BasicTextField(
            value = uiState.editProfile.email,
            onValueChange = {
                resetClickedUpdate()
                onEmailChange(it)
            },
            modifier = Modifier
                .onFocusChanged {
                    onFocusChange = it.hasFocus
                },
            maxLines = 1,
            decorationBox = { innerTextField ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            BorderStroke(
                                1.dp,
                                if (!hasError["email"].isNullOrEmpty() && isClickUpdate)
                                    Color.Red
                                else if (onFocusChange)
                                    Blue1
                                else
                                    Gray65.copy(0.5f)
                            ),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (uiState.editProfile.name.isEmpty()) {
                        Text(
                            "Hãy nhập Email của bạn",
                            color = Color.Black.copy(0.5f)
                        )
                    }

                    innerTextField()
                }
            }
        )
        Spacer(Modifier.height(10.dp))

        if (isClickUpdate && !hasError["email"].isNullOrEmpty()) {
            Text(
                text = hasError["email"] ?: "...",
                modifier = Modifier
                    .padding(start = 22.dp),
                fontStyle = FontStyle.Italic,
                color = Color.Red
            )
        }
    }
//    if (true) {
//        Text(
//            text = "loi khong xac dinh",
//            modifier = Modifier
//                .padding(start = 6.dp),
//            fontStyle = FontStyle.Italic,
//            color = Color.Red
//        )
//    }
//    Spacer(Modifier.height(10.dp))
//
//    Text(
//        text = "Bạn sẽ nhận được lịch sử chuyến đi, lịch sử đơn hàng, và hóa đơn chuyến đi qua địa chỉ Email này.",
//        modifier = Modifier
//            .padding(start = 6.dp),
//        fontStyle = FontStyle.Italic,
//        color = Color.Black.copy(0.6f)
//    )
}


@Preview
@Composable
fun PreviewEmailInfoSelection() {
    Box(Modifier.fillMaxSize().background(Color.White))
    EmailInfoSelection(
        uiState = ProfileUiState(),
        onEmailChange = {},
        hasError = mapOf(),
        isClickUpdate = true,
        resetClickedUpdate = {},
        )
}