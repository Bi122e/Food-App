package com.example.foodapp.ui.screen.initializationInfo.progressStep

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.R
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray65

@Composable
fun NameStep(
    profileUiState: ProfileUiState,
    onFieldEditProfileChange: (String, String) -> Unit,
    onNextStep: () -> Unit,
     resetClickedUpdate: () -> Unit,
    setClickedUpdate: () -> Unit,
    validateName: () -> Boolean,
    onValidStep: () -> Unit,
) {


    val name = profileUiState.editProfile.name
    var inTouch by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))

        Text(
            text = "Nhập tên người dùng của bạn",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Image(
            painter = painterResource(R.drawable.ic_avatar1),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .size(70.dp)
        )


        BasicTextField(
            value = name,
            onValueChange = {
                resetClickedUpdate()
                onFieldEditProfileChange("name", it)
            },
            modifier = Modifier
                .onFocusChanged(
                    onFocusChanged = {
                        inTouch = it.isFocused
                    }
                )
                .padding(horizontal = 16.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .border(
                            width = 1.dp, shape = RoundedCornerShape(10.dp),
                            color = if (inTouch)
                                Blue1
                            else if (!validateName() && profileUiState.isClickedUpdate)
                                Color.Red
                            else Gray65.copy(0.5f)
                        )
                        .padding(15.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    innerTextField()
                }
            },
        )

        if (!validateName() && profileUiState.isClickedUpdate) {
            Text(
                text = profileUiState.errorMessage["name"] ?: "...",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 22.dp),
                fontStyle = FontStyle.Italic,
                color = Color.Red
            )
        }

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .fillMaxWidth()
                .clickable(
                    enabled = profileUiState.editProfile.name.isNotEmpty(),
                    onClick = {
                        setClickedUpdate()
                        if (validateName()) {
                            onValidStep()
                            resetClickedUpdate()
                            onNextStep()
                        }
                    }
                )
                .background(
                    if (name.isNotEmpty()) Blue1 else Blue1.copy(0.3f),
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Tiếp theo",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier

                    .padding(16.dp)
            )
        }

    }
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewNameStep() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    Text("TEST", color = Color.White)

    NameStep(
        onFieldEditProfileChange = { _, _ -> Unit },
        onNextStep = {},
        profileUiState = ProfileUiState(),
         resetClickedUpdate = {},
        validateName = { false },
        setClickedUpdate = {},
        onValidStep = {},
    )
}