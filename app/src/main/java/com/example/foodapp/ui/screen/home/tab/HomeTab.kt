package com.example.foodapp.ui.screen.home.tab


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.foodapp.core.UiState
import com.example.foodapp.core.utils.displayAddress
import com.example.foodapp.domain.model.Food
import com.example.foodapp.presentation.viewmodel.HomeViewModel
import com.example.foodapp.presentation.viewmodel.ProfileViewModel
import com.example.foodapp.ui.theme.PrimaryBlue

@Composable
fun HomeTab(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()

) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val address = uiState.editProfile.address

    val query by homeViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResult by homeViewModel.searchResult.collectAsStateWithLifecycle()

    HomeTabContent(
        address, query = query, searchResult = searchResult
    ) { query ->
        homeViewModel.updateSearchQuery(query)

    }
}

@Composable
fun HomeTabContent(
    address: String,
    query: String,
    searchResult: UiState<List<Food>>,
    onQueryChange: (String) -> Unit
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
                    HeaderHome(address)
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


@Composable
private fun HeaderHome(
    address: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            tint = Color.White,
            modifier = Modifier.size(32.dp),
            contentDescription = null
        )
        Spacer(Modifier.width(4.dp))


        Text(
            text = address.displayAddress(),
            maxLines = 1,
            fontSize = 16.sp,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
        Spacer(Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Notifications,
                tint = Color.White,
                contentDescription = null,
            )
        }

    }
}

@Composable
fun Search(
    query: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,   // chỉ dùng ở đây
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 6.dp,
        color = Color.White
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onValueChange,
            placeholder = { Text("Tìm kiếm…") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()   // KHÔNG dùng modifier ở đây
        )
    }
}



@Composable
fun SearchFoodItem(
    food: Food, modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            // Ảnh
            AsyncImage(
                model = food.imgUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "${food.price} đ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryBlue
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "⭐ ${food.totalRating} (${food.reviews})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview
@Composable
fun HomeTabPreview() {
    HomeTabContent(
        "", "", UiState.Success(emptyList()), {})
}