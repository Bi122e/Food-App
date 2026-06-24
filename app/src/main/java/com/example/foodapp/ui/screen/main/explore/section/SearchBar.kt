package com.example.foodapp.ui.screen.main.explore.section

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodapp.core.utils.limit
import com.example.foodapp.core.utils.showToast
import com.example.foodapp.presentation.extentions.toConvertEscapeTag


@Composable
fun SearchBar(
    text: String,
    tagHolder: String?,
    onNavigationToExplore: () -> Unit,
    onTextChange: (String) -> Unit,
     ) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current //dong ban phim khi search
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        singleLine = true,
        value = text.limit(23),
        onValueChange = {
            onTextChange(it)
        },
        modifier = Modifier
            .padding(end = 23.dp)
            .height(45.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .onFocusChanged {
                isFocused = it.isFocused
            },
        textStyle = LocalTextStyle.current.copy(
            color = Color.Black,
            fontSize = 14.sp
        ),
        //keyboardOptions
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                showToast(context, "click")
                keyboardController?.hide()
                onNavigationToExplore()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = if (isFocused   )
                            Color.Black.copy(0.7f)
                        else
                            Color.Black.copy(0.1f),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Box(modifier = Modifier.weight(1f)) {
                    if (text.isEmpty()) {
                        Text(
                            text = tagHolder.toConvertEscapeTag(),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color.Black.copy(0.4f)
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchBar(
        text = "",
        onTextChange = {},
        tagHolder = "choa coa",
        onNavigationToExplore = {},
      )
}