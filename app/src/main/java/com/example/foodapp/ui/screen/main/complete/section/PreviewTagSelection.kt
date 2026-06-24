package com.example.foodapp.ui.screen.main.complete.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coloredShadow
import com.example.foodapp.presentation.state.CompleteUiState
import com.example.foodapp.ui.theme.Blue0
import com.example.foodapp.ui.theme.Gray65

@Composable
fun PreviewTagSelection(
    uiState: CompleteUiState,
    onAddPreviewTag: (String) -> Unit,
    onRemovePreviewTag: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Bạn ấn tượng quán về điều gì?",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        val items = listOf("Ngon đỉnh", "Giá phải chăng", "Đóng gói đẹp", "Phần ăn no căng")
        val selectedItems = uiState.previewTags

        items.chunked(2).forEach { rowItems ->


            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ) {
                rowItems.forEach { item ->
                    val isSelected = item in selectedItems


                    Box(
                        modifier = Modifier
//                            .border(
//                                BorderStroke(1.dp, Gray100),
//                                RoundedCornerShape(20.dp)
//                            )
                            .coloredShadow(
                                colors = listOf(Gray65.copy(0.9f)),
                                alpha = 0.5f, //độ đậm
                                blurRadius = 3.dp, //lan tỏa
                                borderRadius = 20.dp
                            )
                            .background(
                                if (isSelected)
                                    Color(0xFFeffbfb)
                                else
                                    Color.White,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable(
                                onClick = {
                                    if (isSelected)
                                        onRemovePreviewTag(item)
                                    else
                                        onAddPreviewTag(item)
                                }
                            )
                            .padding(6.dp)
                    ) {
                        Text(
                            modifier = Modifier,
                            text = item,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isSelected) {
                                Blue0
                            } else {
                                Color.Black
                            }
                        )
                    }
                }
            }
        }

    }

}