package com.example.foodapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodapp.core.UserRoutes
import com.example.foodapp.core.bottomRouteFromIndex
import com.example.foodapp.ui.components.HomeBottomBar
import com.example.foodapp.ui.theme.PrimaryBlue
import com.example.foodapp.ui.theme.secondBlue


@Composable
fun HomeScreen() {

    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            HomeBottomBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index

                    navController.navigate(bottomRouteFromIndex(index)) {
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(UserRoutes.HOME) {
                            saveState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = UserRoutes.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(UserRoutes.HOME) {
                HomeTab()
            }

            composable(UserRoutes.CHAT) {
                ChatTab()
            }

            composable(UserRoutes.CART) {
                CartTab()
            }

            composable(UserRoutes.PROFILE) {
                ProfileTab()
            }
        }
    }
}



@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to secondBlue,
                        0.10f to PrimaryBlue,
                        0.2f to Color.White,
                        1f to Color.White
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(30) { index ->
                Text(
                    text = "Item $index",
                    modifier = Modifier.padding(12.dp),
                    color = Color.Black
                )
            }
        }
    }
}
@Composable
fun HomeTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("🏠 Trang chủ")
    }
}

@Composable
fun ChatTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("💬 Chat")
    }
}

@Composable
fun CartTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("🛒 Giỏ hàng")
    }
}

@Composable
fun ProfileTab() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("👤 Hồ sơ")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}