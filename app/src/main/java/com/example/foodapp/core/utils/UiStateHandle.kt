package com.example.foodapp.core.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.foodapp.R
import com.example.foodapp.core.UiErrorType
import com.example.foodapp.core.UiState

@Composable
fun <T> UiStateHandler(
    //nếu uistate có trạng thái nào, hàm handler này sẽ xử lý để hiển thị ảnh tương ứng
    uiState: UiState<T>,
    onLoading: @Composable () -> Unit = { DefaultLoading() },
    onError: @Composable (String, UiErrorType) -> Unit = { _, _ -> DefaultError() },
    onEmpty: @Composable (String) -> Unit = { _ -> DefaultEmpty()},
    onSuccess: @Composable (T) -> Unit

) {
    when (uiState) {
        is UiState.Loading -> onLoading()
        is UiState.Error -> onError(uiState.message, uiState.type)
        is UiState.Empty -> onEmpty(uiState.message)
        is UiState.Success -> onSuccess(uiState.data)
        is UiState.Idle -> { }
    }

}
@Composable
fun DefaultLoading() {
    Image(
        painter = painterResource(R.drawable.bg_box2),
        modifier = Modifier.fillMaxWidth(),
        contentDescription = null
    )
}
@Composable
fun DefaultEmpty() {
    Image(
        painter = painterResource(R.drawable.bg_empty1),
        contentDescription = null
    )
}

@Composable
fun DefaultError() {
    Image(
        painter = painterResource(R.drawable.bg_error1),
        contentDescription = null
    )
}


