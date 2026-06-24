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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray65


@Composable
fun PhoneInfoSelection(
    resetClickedUpdate: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    phoneNumber: String,
    hasError: Map<String, String?>,
    isClickedUpdate: Boolean,
) {

    var onFocusChange by remember { mutableStateOf(false) }
    LaunchedEffect(isClickedUpdate) {
        if (isClickedUpdate) {
            onFocusChange = false
        }
    }
    Column() {

        Text(
            text = "Số điện thoại",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(15.dp))


        BasicTextField(
            value = phoneNumber,
            onValueChange = {
                resetClickedUpdate()
                onPhoneNumberChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier
                .onFocusChanged(
                    onFocusChanged = {
                        onFocusChange = it.hasFocus

                    }
                ),
            maxLines = 1,

            decorationBox = { innerTextField ->


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            BorderStroke(
                                1.dp,
                                if (onFocusChange)
                                    Blue1
                                else if (!hasError["phone"].isNullOrEmpty() && isClickedUpdate)
                                    Color.Red
                                else
                                    Gray65.copy(0.5f)
                            ),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (phoneNumber.isEmpty()) {
                        Text(
                            "Hãy nhập số điện thoại của bạn",
                            color = Color.Black.copy(0.5f)
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(Modifier.height(10.dp))

        if (!hasError["phone"].isNullOrEmpty() && isClickedUpdate) {
            Text(
                text = hasError["phone"] ?: "...",
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
fun PreviewPhoneInfoSelection() {
    Box(Modifier
        .fillMaxSize()
        .background(Color.White))

    PhoneInfoSelection(
        resetClickedUpdate = {},
        onPhoneNumberChange = {},
        phoneNumber = "f",
        hasError = mapOf("phone" to "loi gi day"),
        isClickedUpdate = true
    )

}