package com.example.foodapp.ui.screen.main.profile.section.info

import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import com.example.foodapp.presentation.state.ProfileUiState
import com.example.foodapp.ui.theme.Blue1
import com.example.foodapp.ui.theme.Gray100
import com.example.foodapp.ui.theme.Gray65
import org.w3c.dom.Text
import kotlin.text.isNullOrEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSection(

    expanded: Boolean,
    onExpandedChange: () -> Unit,
    onSelectedGenderChange: (String) -> Unit,
    gender: String,
    setExpandedChange: () -> Unit,
    isClickedUpdate: Boolean,
    resetClickedUpdate: () -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    var onFocusChange by remember { mutableStateOf(false) }

    LaunchedEffect(isClickedUpdate) {
        if (isClickedUpdate) {
            onFocusChange = false
        }
    }

    Column() {
        Text(
            text = "Giới tính",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(Modifier.height(15.dp))
        Log.d("check_update_gender", "selected: ${gender}")

        val option = listOf("Anh", "Chị", "Không muốn tiết lộ")

        ExposedDropdownMenuBox(
            expanded = expanded,
            modifier = Modifier
                .onFocusChanged(
                    onFocusChanged = {
                        onFocusChange = if (isPreview) {
                            true
                        } else {
                            it.hasFocus

                        }
                    }
                ),
            onExpandedChange = { onExpandedChange() },
        ) {

            BasicTextField(
                value = gender,
                onValueChange = {},
                maxLines = 1,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .menuAnchor(),
                readOnly = true,
                decorationBox = { innerTextField ->

// && !isClickedUpdate
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .border(
                                BorderStroke(
                                    1.dp,
                                    // click true  > blue < clicked btn (false)
                                    if (onFocusChange)
                                        Blue1
                                    else
                                        Gray65.copy(0.5f)
                                ),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Rounded.ArrowDropUp
                            else
                                Icons.Rounded.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .align(Alignment.CenterEnd)

                        )

                        innerTextField()
                    }
                }
            )


            ExposedDropdownMenu(

                expanded = expanded,
                onDismissRequest = {
                    setExpandedChange()
                },
                shape = RoundedCornerShape(20.dp),
                containerColor = Color.White,

                ) {

                option.forEachIndexed { index, item ->

                    DropdownMenuItem(

                        text = {
                            Text(
                                text = item,
                            )
                        },
                        onClick = {
                            Log.d("check_update_gender", "item = ${item}")
                            resetClickedUpdate()
                            onSelectedGenderChange(item)
                            setExpandedChange()
                        },
                    )
                    if (option.lastIndex != index) {
                        Spacer(
                            Modifier
                                .height(1.dp)
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .background(Gray100)
                        )
                    }
                }

            }

        }
    }
}


@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewGenderSection() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    )
    GenderSection(
        expanded = true,
        onExpandedChange = {},
        onSelectedGenderChange = { "" },
        gender = "Gioi tinh",
        setExpandedChange = {},
        isClickedUpdate = true,
        resetClickedUpdate = {},
    )
}