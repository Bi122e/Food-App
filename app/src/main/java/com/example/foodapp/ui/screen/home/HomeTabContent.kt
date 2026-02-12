package com.example.foodapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.foodapp.core.UiState
import com.example.foodapp.domain.model.Food
 import com.example.foodapp.ui.theme.PrimaryBlue


@Composable
 fun HomeTabContent(
    address: String,
    query: String,
    searchResult: UiState<List<Food>>,
    headerHome: @Composable (String) -> Unit,
    onQueryChange: (String) -> Unit,

    ) {
//    var query by remember { mutableStateOf(query) }
//    var query by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val isSearchActive = query.isNotBlank()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to Color(0xff32c4e1),
                        0.10f to PrimaryBlue,
                        0.3f to Color(0xffebecee),
                        0.7f to Color(0xfff8f9fb)
                    )
                )
            )
    ) {

//            item {
//                HeaderHome(address)
//                Spacer(Modifier.height(20.dp))
////                Search(query) {
////                    query = it
////                    onQueryChange(query)}
//                Search(
//                    query = query,
//                    onValueChange = onQueryChange
//                )
//            }
//
//            when (searchResult) {
//                is UiState.Success -> {
//                    val foods = searchResult.data
////                        items(foods) { food ->
////                            Text(food.name)
////                        }
//                    items(searchResult.data) { food ->
//                        SearchFoodItem(food = food)
//                    }
//                }
//
//                is UiState.Error -> {
//                    item { Text("Lỗi") }
//                }
//                is UiState.Loading -> {
//                    item { Text("Đang tìm kiếm...") }
//                }
//
//                else -> null

        { }
        if (isSearchActive) {
            // ================= SEARCH MODE =================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Search(
                        query = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "Huỷ",
                        color = PrimaryBlue,
                        modifier = Modifier
//                                .padding(start = 4.dp)
                            .clickable {
                                onQueryChange("")
                                focusManager.clearFocus()
                            })
                }

                Spacer(Modifier.height(16.dp))

                when (searchResult) {
                    is UiState.Success -> {
                        LazyColumn {
                            items(searchResult.data) { food ->
                                SearchFoodItem(food)
                            }
                        }
                    }

                    is UiState.Loading -> {
                        Text("Đang tìm kiếm...")
                    }

                    is UiState.Error -> {
                        Text("Có lỗi xảy ra")
                    }

                    else -> {}
                }
            }

        } else {
            // ================= NORMAL HOME =================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                item {
                    headerHome(address)
                    Spacer(Modifier.height(20.dp))

                    Search(
                        query = query, onValueChange = onQueryChange
                    )
                }

                items(10) {
                    Spacer(Modifier.height(80.dp))
                }

            }

        }
    }
}