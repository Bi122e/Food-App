package com.example.foodapp.ui.screen.main.complete.section

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray65


@Composable
fun BottomCompleteBar(
    uiState: CompleteUiState,
    onChangedPrivate: (Boolean) -> Unit,
    onCreateComplete: () -> Unit,
) {

    val context = LocalContext.current
    var showExtendBox by remember { mutableStateOf(false) }
    val isChecked = uiState.isPrivateName
    val offsetX by animateDpAsState(
        targetValue = if (isChecked) 18.dp else 0.dp,
        label = ""
    )
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(bottom = 16.dp)
    ) {

        //line
        Spacer(
            Modifier
                .height(3.dp)
                .fillMaxWidth()
                .background(Gray65.copy(0.1f))
        )

        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Đánh giá ẩn danh",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(0.8f),
                )

                Spacer(Modifier.width(5.dp))

                Icon(
                    imageVector = Icons.Outlined.Info,
                    tint = Blue0,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        onClick = {
                            showExtendBox = true
                        })
                )

                //extent box
                if (showExtendBox) {

                    Box(
                        modifier = Modifier
                            .offset(
                                x = (-25).dp,
                                y = (-30).dp
                            )
                            .clickable(
                                onClick = {
                                    showToast(context, "click")
                                    showExtendBox = false
                                }
                            )
                            .border(
                                BorderStroke(
                                    1.dp, Gray65.copy(0.4f)
                                ), RoundedCornerShape(
                                    15.dp
                                )
                            )
                            .background(
                                color = Color.White, shape = RoundedCornerShape(20.dp)
                            )


                    ) {
                       //chua
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            //check btn
            Box(
                modifier = Modifier
                    .width(45.dp)
                    .height(25.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isChecked) Blue1 else Color.LightGray
                    )
                    .clickable(
                        onClick = {
                            onChangedPrivate(!isChecked)
                        }
                    )
                    .padding(5.dp)
            ) {

                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = if (isChecked) Blue0 else Color.LightGray,
                    modifier = Modifier
                        .offset(x = offsetX)
                        .size(18.dp)
                        .shadow(
                            elevation = 0.5.dp, RoundedCornerShape(7.dp)
                        )
                        .background(
                            color = Color.White, RoundedCornerShape(7.dp)
                        )
                        .padding(1.dp)

                )

            }

        }


        val canClicked = (uiState.rating ?: 0) > 0

        Button(
            enabled = canClicked,
            onClick = {
                onCreateComplete()
            },
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue1,
                disabledContainerColor = Blue1.copy(0.3f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                "Gửi đánh giá",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(7.dp)
            )
        }
    }
}