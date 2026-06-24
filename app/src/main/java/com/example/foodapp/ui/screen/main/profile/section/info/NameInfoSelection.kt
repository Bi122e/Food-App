package com.example.foodapp.ui.screen.main.profile.section.info

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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


@SuppressLint("RememberReturnType")
@Composable
fun NameInfoSelection(
    uiState: ProfileUiState,
    onNameChange: (String) -> Unit,
    hasError: Map<String, String?>,
    isClickUpdate: Boolean,
    resetClickedUpdate: () -> Unit,
) {
    Log.d("check_can_click_btn", "click update ${uiState.isClickedUpdate}")

    var onFocusChange by remember { mutableStateOf(false) }

    LaunchedEffect(isClickUpdate) {
        if (isClickUpdate) {
            onFocusChange = false
        }
    }
    Column() {
        Text(
            text = "Họ và tên",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(15.dp))


        BasicTextField(
            value = uiState.editProfile.name,
            onValueChange = {
                 resetClickedUpdate()
                onNameChange(it)
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
                                if (!hasError["name"].isNullOrEmpty() && isClickUpdate)
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
                            "Hãy nhập tên của bạn",
                            color = Color.Black.copy(0.5f)
                        )
                    }

                    innerTextField()
                }
            }
        )
        Spacer(Modifier.height(10.dp))

        if (isClickUpdate && !hasError["name"].isNullOrEmpty()) {
            Text(
                text = hasError["name"] ?: "...",
                modifier = Modifier
                    .padding(start = 22.dp),
                fontStyle = FontStyle.Italic,
                color = Color.Red
            )
        }
    }


}

@Preview
@Composable
fun PreviewNameInfoSelection() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    NameInfoSelection(
        uiState = ProfileUiState(),
        onNameChange = {},
        hasError = mapOf(),
        isClickUpdate = true,
        resetClickedUpdate = {},
        )
}