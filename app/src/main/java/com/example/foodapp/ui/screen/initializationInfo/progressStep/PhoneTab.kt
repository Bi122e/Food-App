package com.example.foodapp.ui.screen.initializationInfo.progressStep

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.foodapp.R
 import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Blue2
import com.example.foodapp.ui.theme.Blue4
import com.example.foodapp.ui.theme.Gray65

@Composable
fun PhoneTab(
    validatePhone: () -> Boolean,
    onNextStep: () -> Unit,
    profileUiState: ProfileUiState,
    onFieldEditProfileChange: (String, String) -> Unit,
    setClickedUpdate: () -> Unit,
    resetClickedUpdate: () -> Unit,
    onValidStep: () -> Unit,
) {


    val error by remember { mutableStateOf(true) }
    val textError by remember { mutableStateOf("error") }
    var inTouch by remember { mutableStateOf(false) }
    val canClickBtn by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Image(
            painter = painterResource(R.drawable.bg_mobilephone),
            contentDescription = null,
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = "Nhập số điện thoại của bạn",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(30.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)

        ) {

            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, Gray65.copy(0.5f)),
                        RoundedCornerShape(10.dp)
                    )

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    ),
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_vn),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "+84",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black.copy(0.6f)
                    )
                }
            }
            BasicTextField(
                value = profileUiState.editProfile.phone,
                singleLine = true,
                onValueChange = {
                    resetClickedUpdate()
                    onFieldEditProfileChange(
                        "phone",
                        it.filter { c ->
                            c.isDigit()
                        }.take(10)
                    )
                },
                modifier = Modifier
                    .onFocusChanged {
                        inTouch = it.isFocused
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterHorizontally
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)

                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    BorderStroke(
                                        1.dp, if (inTouch)
                                            Blue1
                                        else if (!validatePhone() && profileUiState.isClickedUpdate)
                                            Color.Red
                                        else
                                            Gray65.copy(0.5f)
                                    ),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart

                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(
                                    10.dp,
                                    Alignment.CenterHorizontally
                                ),
                                modifier = Modifier.padding(vertical = 16.dp)
                            ) {


                                //text holder
                                if (profileUiState.editProfile.phone.isEmpty()) {
                                    Text(
                                        text = "Nhập số điện thoại của bạn",
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.Black.copy(0.4f),

                                        )
                                }
                            }
                            innerTextField()
                        }
                    }
                }
            )
        }
        Spacer(Modifier.height(5.dp))

        if (!validatePhone() && profileUiState.isClickedUpdate) {
            Text(
                text = profileUiState.errorMessage["phone"] ?: "...",
                color = Color.Red,
                fontStyle = FontStyle.Italic
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        //btn

        Box(
            modifier = Modifier
                .clickable(
                    enabled = profileUiState.editProfile.phone.isNotEmpty(),
                    onClick = {
                        setClickedUpdate()
                        if (validatePhone()) {
                             onNextStep()
                            onValidStep()
                            resetClickedUpdate()
//                            onNextNavigationToHome()
                        }
                    }
                )
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .background(
                    if (profileUiState.editProfile.phone.isEmpty())
                        Blue1.copy(0.4f)
                    else Blue1,
                    RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tiếp theo",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        }

        Text(
            text = "Bằng việc nhấn tiếp theo. Tôi đã đồng ý với quy định quy chế sàn TMĐT",
            color = Color.Black.copy(0.4f),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 30.dp)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewPhoneTab() {
    PhoneTab(
        validatePhone = { false },
        profileUiState = ProfileUiState(),
        onFieldEditProfileChange = { _, _ -> Unit },
        setClickedUpdate = {},
        resetClickedUpdate = {},
        onNextStep = {},
        onValidStep = {},
    )


}
